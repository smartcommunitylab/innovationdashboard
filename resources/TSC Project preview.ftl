<#assign portletURL = renderResponse.createRenderURL() />

<#if entries?has_content>
    <ul class="cat-projects-previews">
    	<#list entries as curEntry>
    	    ${portletURL.setParameter("resetCur", "true")}
            ${portletURL.setParameter("categoryId", stringUtil.valueOf(curEntry.getCategoryIds()[0]))}
    	    <a href="${htmlUtil.escape(portletURL.toString())}">
        	    <li class="cat-project-preview-container">
        	        <div class="cat-project-preview-text">
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
