/******************************************************************************
 * jquery.i18n.properties
 *
 * Dual licensed under the GPL (http://dev.jquery.com/browser/trunk/jquery/GPL-LICENSE.txt) and
 * MIT (http://dev.jquery.com/browser/trunk/jquery/MIT-LICENSE.txt) licenses.
 *
 * @version     1.2.7
 * @url         https://github.com/jquery-i18n-properties/jquery-i18n-properties
 * @inspiration Localisation assistance for jQuery (http://keith-wood.name/localisation.html)
 *              by Keith Wood (kbwood{at}iinet.com.au) June 2007
 *
 *****************************************************************************/

(function ($) {

    $.i18n = {};

    $.i18n.map = {};

    var debug = function (message) {
        window.console && console.log('i18n::' + message);
    };

    $.i18n.properties = function (settings) {

        var defaults = {
            name: 'Messages',
            language: '',
            path: '',
            namespace: null,
            mode: 'vars',
            cache: false,
            debug: false,
            encoding: 'UTF-8',
            async: false,
            callback: null
        };

        settings = $.extend(defaults, settings);

        if (settings.namespace && typeof settings.namespace == 'string') {
            // A namespace has been supplied, initialise it.
            if (settings.namespace.match(/^[a-z]*$/)) {
                $.i18n.map[settings.namespace] = {};
            } else {
                debug('Namespaces can only be lower case letters, a - z');
                settings.namespace = null;
            }
        }

        // Ensure a trailing slash on the path
        if (!settings.path.match(/\/$/)) settings.path += '/';

        // Try to ensure that we have at a least a two letter language code
        settings.language = this.normaliseLanguageCode(settings);

        // Ensure an array
        var files = (settings.name && settings.name.constructor === Array) ? settings.name : [settings.name];

        // A locale is at least a language code which means at least two files per name. If
        // we also have a country code, thats an extra file per name.
        settings.totalFiles = (files.length * 2) + ((settings.language.length >= 5) ? files.length : 0);
        if (settings.debug) {
            debug('totalFiles: ' + settings.totalFiles);
        }

        settings.filesLoaded = 0;

        files.forEach(function (file) {

            var defaultFileName, shortFileName, longFileName, fileNames;
            // 1. load base (eg, Messages.properties)
            defaultFileName = settings.path + file + '.properties';
            // 2. with language code (eg, Messages_pt.properties)
            var shortCode = settings.language.substring(0, 2);
            shortFileName = settings.path + file + '_' + shortCode + '.properties';
            // 3. with language code and country code (eg, Messages_pt_BR.properties)
            if (settings.language.length >= 5) {
                var longCode = settings.language.substring(0, 5);
                longFileName = settings.path + file + '_' + longCode + '.properties';
                //fileNames = [defaultFileName, shortFileName, longFileName];
                fileNames = [shortFileName];
            } else {
                //fileNames = [defaultFileName, shortFileName];
            	fileNames = [shortFileName];
            }
            loadAndParseFiles(fileNames, settings);
        });

        // call callback
        if (settings.callback && !settings.async) {
            settings.callback();
        }
    }; // properties

    /**
     * When configured with mode: 'map', allows access to bundle values by specifying its key.
     * Eg, jQuery.i18n.prop('com.company.bundles.menu_add')
     */
    $.i18n.prop = function (key /* Add parameters as function arguments as necessary  */) {

        var args = [].slice.call(arguments);

        var phvList, namespace;
        if (args.length == 2) {
            if ($.isArray(args[1])) {
                // An array was passed as the second parameter, so assume it is the list of place holder values.
                phvList = args[1];
            } else if (typeof args[1] === 'object') {
                // Second argument is an options object {namespace: 'mynamespace', replacements: ['egg', 'nog']}
                namespace = args[1].namespace;
                var replacements = args[1].replacements;
                args.splice(-1, 1);
                if (replacements) {
                    Array.prototype.push.apply(args, replacements);
                }
            }
        }

        var value = (namespace) ? $.i18n.map[namespace][key] : $.i18n.map[key];
        if (value === null) {
            return '[' + ((namespace) ? namespace + '#' + key : key) + ']';
        }

        var i;
        if (typeof(value) == 'string') {
            // Handle escape characters. Done separately from the tokenizing loop below because escape characters are
            // active in quoted strings.
            i = 0;
            while ((i = value.indexOf('\\', i)) != -1) {
                if (value.charAt(i + 1) == 't') {
                    value = value.substring(0, i) + '\t' + value.substring((i++) + 2); // tab
                } else if (value.charAt(i + 1) == 'r') {
                    value = value.substring(0, i) + '\r' + value.substring((i++) + 2); // return
                } else if (value.charAt(i + 1) == 'n') {
                    value = value.substring(0, i) + '\n' + value.substring((i++) + 2); // line feed
                } else if (value.charAt(i + 1) == 'f') {
                    value = value.substring(0, i) + '\f' + value.substring((i++) + 2); // form feed
                } else if (value.charAt(i + 1) == '\\') {
                    value = value.substring(0, i) + '\\' + value.substring((i++) + 2); // \
                } else {
                    value = value.substring(0, i) + value.substring(i + 1); // Quietly drop the character
                }
            }

            // Lazily convert the string to a list of tokens.
            var arr = [], j, index;
            i = 0;
            while (i < value.length) {
                if (value.charAt(i) == '\'') {
                    // Handle quotes
                    if (i == value.length - 1) {
                        value = value.substring(0, i); // Silently drop the trailing quote
                    } else if (value.charAt(i + 1) == '\'') {
                        value = value.substring(0, i) + value.substring(++i); // Escaped quote
                    } else {
                        // Quoted string
                        j = i + 2;
                        while ((j = value.indexOf('\'', j)) != -1) {
                            if (j == value.length - 1 || value.charAt(j + 1) != '\'') {
                                // Found start and end quotes. Remove them
                                value = value.substring(0, i) + value.substring(i + 1, j) + value.substring(j + 1);
                                i = j - 1;
                                break;
                            } else {
                                // Found a double quote, reduce to a single quote.
                                value = value.substring(0, j) + value.substring(++j);
                            }
                        }

                        if (j == -1) {
                            // There is no end quote. Drop the start quote
                            value = value.substring(0, i) + value.substring(i + 1);
                        }
                    }
                } else if (value.charAt(i) == '{') {
                    // Beginning of an unquoted place holder.
                    j = value.indexOf('}', i + 1);
                    if (j == -1) {
                        i++; // No end. Process the rest of the line. Java would throw an exception
                    } else {
                        // Add 1 to the index so that it aligns with the function arguments.
                        index = parseInt(value.substring(i + 1, j));
                        if (!isNaN(index) && index >= 0) {
                            // Put the line thus far (if it isn't empty) into the array
                            var s = value.substring(0, i);
                            if (s !== "") {
                                arr.push(s);
                            }
                            // Put the parameter reference into the array
                            arr.push(index);
                            // Start the processing over again starting from the rest of the line.
                            i = 0;
                            value = value.substring(j + 1);
                        } else {
                            i = j + 1; // Invalid parameter. Leave as is.
                        }
                    }
                } else {
                    i++;
                }
            } // while

            // Put the remainder of the no-empty line into the array.
            if (value !== "") {
                arr.push(value);
            }
            value = arr;

            // Make the array the value for the entry.
            if (namespace) {
                $.i18n.map[settings.namespace][key] = arr;
            } else {
                $.i18n.map[key] = arr;
            }
        }

        if (value.length === 0) {
            return "";
        }
        if (value.length == 1 && typeof(value[0]) == "string") {
            return value[0];
        }

        var str = "";
        for (i = 0, j = value.length; i < j; i++) {
            if (typeof(value[i]) == "string") {
                str += value[i];
            } else if (phvList && value[i] < phvList.length) {
                // Must be a number
                str += phvList[value[i]];
            } else if (!phvList && value[i] + 1 < args.length) {
                str += args[value[i] + 1];
            } else {
                str += "{" + value[i] + "}";
            }
        }

        return str;
    };

    function callbackIfComplete(settings) {

        if (settings.debug) {
            debug('callbackIfComplete()');
            debug('totalFiles: ' + settings.totalFiles);
            debug('filesLoaded: ' + settings.filesLoaded);
        }

        if (settings.async) {
            if (settings.filesLoaded === settings.totalFiles) {
                if (settings.callback) {
                    settings.callback();
                }
            }
        }
    }

    function loadAndParseFiles(fileNames, settings) {

        if (settings.debug) debug('loadAndParseFiles');

	    if (fileNames !== null && fileNames.length > 0) {
		    loadAndParseFile(fileNames[0], settings, function () {
			    fileNames.shift();
			    loadAndParseFiles(fileNames,settings);
		    });
	    } else {
            callbackIfComplete(settings);
        }
    }

    /** Load and parse .properties files */
    function loadAndParseFile(filename, settings, nextFile) {

        if (settings.debug) {
            debug('loadAndParseFile(\'' + filename +'\')');
            debug('totalFiles: ' + settings.totalFiles);
            debug('filesLoaded: ' + settings.filesLoaded);
        }

  	    if (filename !== null && typeof filename !== 'undefined') {
            $.ajax({
                url: filename,
                async: settings.async,
                cache: settings.cache,
                dataType: 'text',
                success: function (data, status) {

                    if (settings.debug) {
                        debug('Succeeded in downloading ' + filename + '.');
                        debug(data);
                    }

                    parseData(data, settings);
                    nextFile();
                },
                error: function (jqXHR, textStatus, errorThrown) {

                    if (settings.debug) {
                        debug('Failed to download or parse ' + filename + '. errorThrown: ' + errorThrown);
                    }
                    if (jqXHR.status === 404) {
                        settings.totalFiles -= 1;
                    }
                    nextFile();
                }
            });
        }
    }

    /** Parse .properties files */
    function parseData(data, settings) {
    	
    	_MESSAGE = data.split(",");
        settings.filesLoaded += 1;
    }

    /** Make sure namespace exists (for keys with dots in name) */
    // TODO key parts that start with numbers quietly fail. i.e. month.short.1=Jan
    function checkKeyNamespace(key) {

        var regDot = /\./;
        if (regDot.test(key)) {
            var fullname = '';
            var names = key.split(/\./);
            for (var i=0,j=names.length;i<j;i++) {
                var name = names[i];

                if (i > 0) {
                    fullname += '.';
                }

                fullname += name;
                if (eval('typeof ' + fullname + ' == "undefined"')) {
                    eval(fullname + '={};');
                }
            }
        }
    }

    /** Ensure language code is in the format aa_AA. */
    $.i18n.normaliseLanguageCode = function (settings) {

        var lang = settings.language;
        if (!lang || lang.length < 2) {
            if (settings.debug) debug('No language supplied. Pulling it from the browser ...');
            lang = (navigator.languages && navigator.languages.length > 0) ? navigator.languages[0]
                                        : (navigator.language || navigator.userLanguage /* IE */ || 'en');
            if (settings.debug) debug('Language from browser: ' + lang);
        }

        lang = lang.toLowerCase();
        lang = lang.replace(/-/,"_"); // some browsers report language as en-US instead of en_US
        if (lang.length > 3) {
            lang = lang.substring(0, 3) + lang.substring(3).toUpperCase();
        }
        return lang;
    };

    /** Unescape unicode chars ('\u00e3') */
    function unescapeUnicode(str) {

        // unescape unicode codes
        var codes = [];
        var code = parseInt(str.substr(2), 16);
        if (code >= 0 && code < Math.pow(2, 16)) {
            codes.push(code);
        }
        // convert codes to text
        return codes.reduce(function (acc, val) { return acc + String.fromCharCode(val); }, '');
    }
}) (jQuery);
