<#assign portletURL = renderResponse.createRenderURL() />

<div class="cats-squares-container">
    <h1 class="cats-squares-title">Torino diventa intelligente. Torino diventa SMART.</h1>
    <#if entries?has_content>
    	<#list entries as curVocabulary>
    		<#assign cats = curVocabulary.getCategories() />
    		<#if cats?has_content>
    		    <ul class="cats-squares">
        		    <#list 0..cats?size-1 as i>
                        ${portletURL.setParameter("resetCur", "true")}
                        ${portletURL.setParameter("categoryId", stringUtil.valueOf(cats[i].getCategoryId()))}
                        <a href="${htmlUtil.escape(portletURL.toString())}">
                            <li class="cat-square cat-square-${cats[i].getName()?lower_case}">
                                <h3 class="cat-name">${cats[i].getName()}</h3>
                            </li>
                        </a>
                        <#if (i % 4 == 3)>
                            <br />
        		        </#if>
                    </#list>
                </ul>
            </#if>	
    	</#list>
    </#if>
</div>
