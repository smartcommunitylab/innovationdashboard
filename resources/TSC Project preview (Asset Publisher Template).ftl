<#assign Validator = staticUtil["com.liferay.portal.kernel.util.Validator"] />
<#assign TSCU = staticUtil["it.smartcommunitylab.tsc.utils.TscUtil"] />
<#assign colors = TSCU.getStructureFieldValues(siteGroupId, "TSC Category structure", "color", true) />
<#assign squareimages = TSCU.getStructureFieldValues(themeDisplay.getSiteGroupId(), "TSC Category structure", "squareimage", true) />
<#assign images = TSCU.getStructureFieldValues(siteGroupId, "TSC Project structure", "image", false) />
<#assign ambitiPrimari = TSCU.getStructureFieldValues(siteGroupId, "TSC Project structure", "ambitoPrimario", false) />
<#assign abstractProgetti = TSCU.getStructureFieldValues(siteGroupId, "TSC Project structure", "abstractProgetto", false) />

<#if entries?has_content>
    <ul class="cat-projects-previews">
    	<#list entries as curEntry>
    	    <#assign viewURL = assetPublisherHelper.getAssetViewURL(renderRequest, renderResponse, curEntry) />
    	    <#assign entryId = stringUtil.valueOf(curEntry.getEntryId()) />
    	    <#assign ambitoPrimarioJson = ambitiPrimari[entryId]?eval />
            <#assign ambitoPrimarioJsonKeys = ambitoPrimarioJson?keys />
            <#assign ambitoPrimarioName = ambitoPrimarioJsonKeys[0] />
            <#assign categoryId = stringUtil.valueOf(TSCU.getCategoryIdByName(ambitoPrimarioName)) />
            <#assign squareimage = squareimages[categoryId] />
            
            <#assign colorHex = "#DDDDDD" />
            <#if Validator.isNotNull(colors[categoryId]) >
    	        <#assign colorHex = colors[categoryId] />
    	    </#if>
    	    
            <#assign colorRgba = TSCU.hex2rgba(colorHex, 0.7) />
            <#if Validator.isNotNull(images[entryId])>
                <#assign image = images[entryId] />
            <#else>
                <#assign image = '${javascript_folder}/../images/covers/cover_${ambitoPrimarioName?lower_case}.jpg' />
            </#if>
    	    <a href="${viewURL}">
    	        <#if Validator.isNotNull(image) >
    	            <li class="cat-project-preview-container" style="background-image:url(${image})">
    	        <#else>
    	            <li class="cat-project-preview-container">
    	        </#if>
        	        <div class="cat-project-preview-bottom">
        	            <div><img src="${squareimage}" /></div>
        	            <div class="cat-project-preview-bottom-text" style="background-color:${colorRgba}">
                		    <h4 class="cat-project-preview-title">${curEntry.getTitle(locale)}</h4>
                		    <#assign abstractProgetto = abstractProgetti[entryId] />
                		    <p class="cat-project-preview-desc">${abstractProgetto}</p>
            		    </div>
                    </div>
        		</li>
        	</a>
    	</#list>
	</ul>
</#if>
