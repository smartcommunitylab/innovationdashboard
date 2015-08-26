<#assign portletURL = renderResponse.createRenderURL() />
<#assign Validator = staticUtil["com.liferay.portal.kernel.util.Validator"] />
<#assign CU = staticUtil["it.smartcommunitylab.util.CategoriesUtil"] />
<#assign colors = CU.getCategoriesColors(siteGroupId) />
<#assign images = CU.getProjectsImages(siteGroupId) />

<#if entries?has_content>
    <ul class="cat-projects-previews">
    	<#list entries as curEntry>
    	    <#assign categoryId = stringUtil.valueOf(curEntry.getCategoryIds()[0]) />
    	    <#assign colorHex = colors[categoryId] />
            <#assign colorRgba = CU.hex2rgba(colorHex, 0.7) />
            <#assign image = images[stringUtil.valueOf(entry.getEntryId())] />
    	    ${portletURL.setParameter("resetCur", "true")}
            ${portletURL.setParameter("categoryId", categoryId)}
    	    <a href="${htmlUtil.escape(portletURL.toString())}">
    	        <#if Validator.isNotNull(image) >
    	            <li class="cat-project-preview-container" style="background-image:url(${image})">
    	        <#else>
    	            <li class="cat-project-preview-container">
    	        </#if>
        	        <div class="cat-project-preview-text" style="background-color:${colorRgba}">
        	            <div class="cat-project-preview-text-container">
            		    <h4 class="cat-project-preview-title">${curEntry.getTitle(locale)}</h4>
            		    <p class="cat-project-preview-desc">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris sodales auctor orci vitae rutrum. Donec sem justo, lacinia id faucibus eu, dapibus a justo. Suspendisse ac turpis vel enim eleifend tempor ut sed risus. Aliquam...</p>
            		    </div>
                    </div>
        		</li>
        	</a>
    	</#list>
	</ul>
</#if>
