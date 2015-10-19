<#assign liferay_ui = PortalJspTagLibs["/WEB-INF/tld/liferay-ui.tld"] />
<#assign aui = PortalJspTagLibs["/WEB-INF/tld/liferay-aui.tld"] />

<#assign AssetVocabularyLocalServiceUtil = staticUtil["com.liferay.portlet.asset.service.AssetVocabularyLocalServiceUtil"] />
<#assign AssetCategoryLocalServiceUtil = staticUtil["com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil"] />
<#assign categories = AssetCategoryLocalServiceUtil.getCategories() />
<#assign groupId = themeDisplay.getLayout().getGroupId() />

<#assign PortletURLFactoryUtil = staticUtil["com.liferay.portlet.PortletURLFactoryUtil"] />
<#assign LayoutLocalServiceUtil = staticUtil["com.liferay.portal.service.LayoutLocalServiceUtil"] />

<nav class="${nav_css_class}" id="navigation" role="navigation">
	<div class="navigation-row">
		<a class="${logo_css_class}" href="${site_default_url}" title="<@liferay.language_format arguments="${site_name}" key="go-to-x" />">
			<img alt="${logo_description}" height="${site_logo_height}" src="${site_logo}" width="${site_logo_width}" />
		</a>
	
		<ul class="nav-user">
			<#if !is_signed_in>
				<#assign anchorData = {"redirect", portalUtil.isLoginRedirectRequired(request)} />
				<@aui["nav-item"] anchorData=anchorData cssClass="sign-in" anchorCssClass="use-dialog" title="Accedi" href="${themeDisplay.getURLSignIn()}" label="Accedi" />
			<#else>
				<#--
				<#assign id = themeDisplay.getPortletDisplay().getId() />
				<#assign plid = themeDisplay.getPlid() />
				<#assign profileURL = portletURLFactory.create(request, "tsc", plid, "ACTION_PHASE") />
				${profileURL.setParameter("groupId", "" + themeDisplay.getUser().getGroupId())}
				${profileURL.setPortletMode("VIEW")}
				${profileURL.setWindowState("MAXIMIZED")}
				-->
			
				<#--
				<@liferay_util["buffer"] var="userprofile">
					<span class="user-full-name">${user.getFullName()}</span>
					<img class="user-avatar-image" src="${user.getPortraitURL(themeDisplay)}" />
				</@>
				-->

				<@aui["nav-item"] cssClass="user-avatar" dropdown=false id="userAvatar" label="${user.getFullName()}">
					<#--
					<li class="my-sites-menu public-site">
						<a href="${profileURL.toString()}">
							<span class="site-name"><@liferay_ui["message"] key="my-profile" /></span> 
						</a>
					</li>
					-->
					
					<#if themeDisplay.isShowSignOutIcon()>
						<@aui["nav-item"] cssClass="sign-out" href="${themeDisplay.getURLSignOut()}" label="sign-out" />
						<#-- iconCssClass="icon-off" -->
					</#if>
				</@>
			</#if>
		</ul>
		
		<ul class="search">			
			<#-- <@liferay_ui["toggle"] defaultShowContent=false id="searchform" /> -->
			<li class="searchform">
				<@liferay_ui["search"] />
			</li>
			<@aui["nav-item"] href="#" cssClass="searchtoggle" iconCssClass="icon-search" />
		</ul>
	</div>
	
	<div class="navigation-row">
		<ul id="navmenu" class="menubar" aria-label="<@liferay.language key="site-pages" />" role="menubar">
			<#list nav_items as nav_item>
				<#if nav_item.getLayout().getExpandoBridge().hasAttribute(footer) && nav_item.getLayout().getExpandoBridge().getAttribute(footer, false) == false>
					<#assign nav_item_attr_has_popup = "" />
					<#assign nav_item_attr_selected = "" />
					<#assign nav_item_css_class = "" />
		
					<#if nav_item.isSelected()>
						<#assign nav_item_attr_has_popup = "aria-haspopup='true'" />
						<#assign nav_item_attr_selected = "aria-selected='true'" />
						<#assign nav_item_css_class = "selected" />
					</#if>
		
					<li ${nav_item_attr_selected} class="${nav_item_css_class}" id="layout_${nav_item.getLayoutId()}" role="presentation">
						<a aria-labelledby="layout_${nav_item.getLayoutId()}" ${nav_item_attr_has_popup} href="${nav_item.getURL()}" ${nav_item.getTarget()} role="menuitem"><span>${nav_item.icon()} ${nav_item.getName()}</span></a>
		
						<#if nav_item.hasChildren()>
							<ul class="child-menu" role="menu">
								<#list nav_item.getChildren() as nav_child>
									<#assign nav_child_attr_selected = "" />
									<#assign nav_child_css_class = "" />
		
									<#if nav_item.isSelected()>
										<#assign nav_child_attr_selected = "aria-selected='true'" />
										<#assign nav_child_css_class = "selected" />
									</#if>
		
									<li ${nav_child_attr_selected} class="${nav_child_css_class}" id="layout_${nav_child.getLayoutId()}" role="presentation">
										<a aria-labelledby="layout_${nav_child.getLayoutId()}" href="${nav_child.getURL()}" ${nav_child.getTarget()} role="menuitem">${nav_child.getName()}</a>
									</li>
								</#list>
							</ul>
						</#if>
					</li>
					
					<#assign categoryLayout = LayoutLocalServiceUtil.getFriendlyURLLayout(groupId, false, "/category") >
					<#list categories as category>
						<#assign categoryId = stringUtil.valueOf(category.getCategoryId()) />
						<#assign categoryPlid = categoryLayout.getPlid() /> 
						<#assign categoryURL = portletURLFactory.create(request, "101", categoryPlid, "RENDER_PHASE") />
						${categoryURL.setParameter("resetCur", "true")}
						${categoryURL.setParameter("categoryId", categoryId)}
						${categoryURL.setPortletMode("VIEW")}
						${categoryURL.setWindowState("NORMAL")}
						
						<li ${nav_item_attr_selected} class="${nav_item_css_class}" id="layout_${nav_item.getLayoutId()}" role="presentation">
							<a href="${categoryURL.toString()}" role="menuitem"><span>${category.getName()}</span></a>
						</li>
					</#list>
				</#if>
			</#list>
		</ul>
	</div>
</nav>

<script>
	Liferay.Data.NAV_LIST_SELECTOR = '#navmenu';
</script>