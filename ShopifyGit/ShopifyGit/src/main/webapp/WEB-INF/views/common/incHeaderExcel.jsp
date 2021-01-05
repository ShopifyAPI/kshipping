<%@ page language="java"
    contentType="application/vnd.ms-excel; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
    <meta charset="UTF-8" />
    <meta name="_csrf_parameter" content="${_csrf.parameterName}" />
    <meta name="_csrf_header" content="${_csrf.headerName}" />
    <meta name="_csrf" content="${_csrf.token}" />
    
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="initial-scale=1.0, width=device-width">

    <script src="//js.stripe.com/v3/"></script>
    <script src="//widget.cloudinary.com/v2.0/global/all.js" type="text/javascript"></script>  
    <script src="//cdn.shopify.com/s/assets/external/app.js"></script>
    <script src="//code.jquery.com/jquery-1.12.4.js"></script>
	<script src="//code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

    <script src="/js/jquery.i18n.properties.js"></script>
    <script src="/js/jquery.bpopup.min.js"></script>	
    <script src="/js/common.js?v=<%=System.currentTimeMillis() %>"></script>
    <script src="/js/util.js?v=<%=System.currentTimeMillis() %>"></script>
    <script type="text/javascript">
    var _USER_lang = "${SHOPIFY_LOCALE_SESSION.locale}";
	var _MESSAGE;
	</script>
