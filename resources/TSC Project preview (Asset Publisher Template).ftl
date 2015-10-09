<#assign Validator = staticUtil["com.liferay.portal.kernel.util.Validator"] />
<#assign TSCU = staticUtil["it.smartcommunitylab.tsc.utils.TscUtil"] />
<#assign colors = TSCU.getStructureFieldValues(siteGroupId, "TSC Category structure", "color", true) />
<#assign images = TSCU.getStructureFieldValues(siteGroupId, "TSC Project structure", "image", false) />
<#assign ambitiPrimari = TSCU.getStructureFieldValues(siteGroupId, "TSC Project structure", "ambitoPrimario", false) />

<#if entries?has_content>
    <ul class="cat-projects-previews">
    	<#list entries as curEntry>
    	    <#assign viewURL = assetPublisherHelper.getAssetViewURL(renderRequest, renderResponse, curEntry) />
    	    <#assign entryId = stringUtil.valueOf(curEntry.getEntryId()) />
    	    <#assign ambitoPrimarioJson = ambitiPrimari[entryId]?eval />
            <#assign ambitoPrimarioJsonKeys = ambitoPrimarioJson?keys />
            <#assign ambitoPrimarioName = ambitoPrimarioJsonKeys[0] />
            <#assign categoryId = TSCU.getCategoryIdByName(ambitoPrimarioName) />
            
            <#assign colorHex = "#DDDDDD" />
            <#if Validator.isNotNull(colors[stringUtil.valueOf(categoryId)]) >
    	        <#assign colorHex = colors[stringUtil.valueOf(categoryId)] />
    	    </#if>
    	    
            <#assign colorRgba = TSCU.hex2rgba(colorHex, 0.7) />
            <#if Validator.isNotNull(images[entryId])>
                <#assign image = images[entryId] />
            </#if>
    	    <a href="${viewURL}">
    	        <#if Validator.isNotNull(image) >
    	            <li class="cat-project-preview-container" style="background-image:url(${image})">
    	        <#else>
    	            <li class="cat-project-preview-container">
    	        </#if>
        	        <div class="cat-project-preview-text" style="background-color:${colorRgba}">
        	            <div class="cat-project-preview-text-container">
            		    <h4 class="cat-project-preview-title">${curEntry.getTitle(locale)}</h4>
            		    <p class="cat-project-preview-desc">${viewURL}</p>
            		    </div>
                    </div>
        		</li>
        	</a>
    	</#list>
	</ul>
</#if>
