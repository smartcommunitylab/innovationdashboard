<#assign portletURL = renderResponse.createRenderURL() />
<#assign TSCU = staticUtil["it.smartcommunitylab.tsc.utils.TscUtil"] />
<#assign colors = TSCU.getStructureFieldValues(themeDisplay.getSiteGroupId(), "TSC Category structure", "color", true) />
<#assign images = TSCU.getStructureFieldValues(themeDisplay.getSiteGroupId(), "TSC Category structure", "squareimage", true) />

<div class="cats-squares-container">
    <h1 class="cats-squares-title">Torino diventa intelligente. Torino diventa SMART.</h1>
    <#if entries?has_content>
	    <ul class="cats-squares">
		    <#list 0..entries?size-1 as i>
		        <#assign categoryId = stringUtil.valueOf(entries[i].getCategoryIds()[0]) />
		        <#assign color = colors[categoryId] />
		        <#assign image = images[categoryId] />
                ${portletURL.setParameter("resetCur", "true")}
                ${portletURL.setParameter("categoryId", categoryId)}
                <a href="${htmlUtil.escape(portletURL.toString())}">
                    <li class="cat-square" style="background-color:${color};background-image:url(${image})">
                        <h3 class="cat-name">${entries[i].getTitle(locale)}</h3>
                    </li>
                </a>
                <#if (i % 4 == 3)>
                    <br />
		        </#if>
            </#list>
        </ul>
    </#if>
</div>
