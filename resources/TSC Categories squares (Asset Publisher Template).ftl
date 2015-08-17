<#assign portletURL = renderResponse.createRenderURL() />

<div class="cats-squares-container">
    <h1 class="cats-squares-title">Torino diventa intelligente. Torino diventa SMART.</h1>
    <#if entries?has_content>
	<ul class="cats-squares">
	    <#list 0..entries?size-1 as i>
                ${portletURL.setParameter("resetCur", "true")}
                ${portletURL.setParameter("categoryId", stringUtil.valueOf(entries[i].getCategoryIds()[0]))}
                <a href="${htmlUtil.escape(portletURL.toString())}">
                    <li class="cat-square cat-square-${entries[i].getTitle(locale)?lower_case}">
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
