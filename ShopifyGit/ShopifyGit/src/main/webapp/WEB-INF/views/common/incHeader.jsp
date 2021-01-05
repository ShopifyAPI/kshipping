<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <meta charset="UTF-8" />
    <meta name="_csrf_parameter" content="${_csrf.parameterName}" />
    <meta name="_csrf_header" content="${_csrf.headerName}" />
    <meta name="_csrf" content="${_csrf.token}" />
    
    
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="initial-scale=1.0, width=device-width">
    
 <!--   <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon">
      <link rel="icon" href="/favicon.ico" type="image/x-icon">-->
      
       <link rel="shortcut icon" href="//cdn.shopify.com/s/files/1/0278/0738/3690/files/ms-icon-310x310_32x32.png?v=1599026309" type="image/png">
    
    
    <link rel="stylesheet" href="https://unpkg.com/@shopify/polaris@4.10.2/styles.min.css"/>
    <link type="text/css" rel="stylesheet" href="/style/reset.css?v=<%=System.currentTimeMillis() %>" />
    <link type="text/css" rel="stylesheet" href="/style/k_shipping.css?v=<%=System.currentTimeMillis() %>" />
    
    <script src="//js.stripe.com/v3/"></script>
    <script src="//widget.cloudinary.com/v2.0/global/all.js" type="text/javascript"></script>  
    <script src="//cdn.shopify.com/s/assets/external/app.js"></script>
    <script src="//code.jquery.com/jquery-1.12.4.js"></script>
	<script src="//code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
	<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">

    <link type="text/css" rel="stylesheet" href="/style/spinner.css?v=<%=System.currentTimeMillis() %>" />

    <script src="/js/jquery.i18n.properties.js"></script>
<!--     <script src="/js/jquery.i18n.properties.min.js"></script> -->
    <script src="/js/jquery.bpopup.min.js"></script>
    <script src="/js/jquery.form.js"></script>
    <script src="https://unpkg.com/@shopify/app-bridge"></script>
	<script type="module">
	if( window.top.location.host != "localhost" ) {
		showSpinner();
		const redirectUri = "https://${SHOPIFY_SESSION.domain}";
		var urlParams = new URLSearchParams(window.location.search);
		var shopOrigin = urlParams.get('shop');
		//const permissionUrl = '/oauth/authorize?client_id=${SHOPIFY_SESSION.accessKey}&scope${SHOPIFY_SESSION.scope}&redirect_uri=https://localhost/dashboard';
		const permissionUrl = 'https://${SHOPIFY_SESSION.domain}/admin/apps/${SHOPIFY_SESSION.appName}/dashboard';

		if (window.top == window.self) {
		     window.location.assign('https://${SHOPIFY_SESSION.domain}/admin/apps/${SHOPIFY_SESSION.appName}/dashboard');
			//window.location.assign('https://${SHOPIFY_SESSION.domain}'+permissionUrl);
		} else {
		    const app = createApp({
		       apiKey: apiKey,
		       shopOrigin: shopOrigin,
		       forceRedirect: true
		   });
		   console.log('bridge redirect')
		   Redirect.create(app).dispatch(Redirect.Action.ADMIN_PATH, permissionUrl);
		}
		hideSpinner();
	}
	</script>

    <script src="/ckEditor/ckEditor/ckeditor.js"></script>
    <script src="/js/common.js?v=<%=System.currentTimeMillis() %>"></script>
    <script src="/js/util.js"></script>
<%--     <script src="/js/util.js?v=<%=System.currentTimeMillis() %>"></script> --%>
    <script type="text/javascript">
    var _USER_lang = "${SHOPIFY_LOCALE_SESSION.locale}";
	var _MESSAGE;
	</script>
