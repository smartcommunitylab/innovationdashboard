<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui" %>

<portlet:defineObjects />

<portlet:actionURL name="uploadData" var="uploadDataURL" />
<aui:form  enctype="multipart/form-data"
    action="<%=uploadDataURL%>" method="post">
    <aui:fieldset cssClass="simple-field" label="dashboard_form_file">
        <aui:input name="file" type="file"></aui:input>    
    </aui:fieldset>


    <aui:button-row  cssClass="formbutton-row">
    <aui:button cssClass="formbutton-primary" type="submit"></aui:button>
    </aui:button-row>
</aui:form> 