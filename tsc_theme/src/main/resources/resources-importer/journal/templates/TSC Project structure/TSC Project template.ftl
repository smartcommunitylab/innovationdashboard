<#assign Validator = staticUtil["com.liferay.portal.kernel.util.Validator"] />
<#assign ACLSU = staticUtil["com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil"] />
<#assign CU = staticUtil["it.smartcommunitylab.tsc.util.CategoriesUtil"] />
<#assign colors = CU.getCategoriesColors(siteGroupId) />
<#assign colorHex = colors[request.parameters.categoryId] />
<#assign colorRgba = CU.hex2rgba(colorHex, 0.5) />
<#assign status1 = getterUtil.getBoolean(status_approved_waiting.getData())?string('done', '') />
<#assign status2 = getterUtil.getBoolean(status_approved_doing.getData())?string('done', '') />
<#assign status3 = getterUtil.getBoolean(status_completed.getData())?string('done', '') />

<h1>${stringUtil.valueOf(.vars['reserved-article-title'].data)}</h1>
<p>PAROLE CHIAVE: </p>
<div class="row-fluid">
    <div class="span6">
        <ul class="project-summary" style="border-color:${colorHex}">
            <li class="project-summary-title">
                in sintesi
            </li>
            <#if Validator.isNotNull(project_where.getData())>
                <li class="project-summary-where">
                    <i class="smart-where"></i>
                    ${project_where.getData()}
                </li>
            </#if>
            <#if Validator.isNotNull(project_what.getData())>
                <li class="project-summary-what">
                    <i class="smart-what"></i>
                    ${project_what.getData()}
                </li>
            </#if>
            <li class="project-summary-category">
                <i class="smart-category"></i>
                ${ACLSU.getCategory(request.parameters.categoryId?number).getName()}
            </li>
            <#if Validator.isNotNull(project_innovation.getData())>
                <li class="project-summary-innovation">
                    <i class="smart-innovation"></i>
                    ${project_innovation.getData()}
                </li>
            </#if>
            <#if Validator.isNotNull(project_financing.getData())>
                <li class="project-summary-financing">
                    <i class="smart-financing"></i>
                    ${project_financing.getData()}
                </li>
            </#if>
            <#if Validator.isNotNull(project_website.getData())>
                <li class="project-summary-website">
                    <i class="smart-website"></i>
                    <a href="${project_website.getData()}">${project_website.getData()}</a>
                </li>
            </#if>
            <#if Validator.isNotNull(project_masterplan.getData())>
                <li class="project-summary-masterplan">
                    <i class="smart-masterplan"></i>
                    <a href="${project_masterplan.getData()}">${project_website.getData()}</a>
                </li>
            </#if>
        </ul>
        <div class="project-description">${description.getData()}</div>
    </div>
    <div class="span6">
        <h6 class="project-update">Dettagli aggiornati al</h6>
        <h6 class="project-progress-title">Stato di avanzamento</h6>
        <div class="row-fluid project-progress-container">
            <div class="span4 project-progress-step project-progress-step-1 ${status1}">
                <hr />
                <div class="project-progress-circle">1</div>
                <div class="project-progress-circle-text">Approvato e in attesa di avvio</div>
            </div>
            <div class="span4 project-progress-step project-progress-step-2 ${status2}">
                <hr />
                <div class="project-progress-circle">2</div>
                <div class="project-progress-circle-text">Approvato e in sviluppo</div>
            </div>
            <div class="span4 project-progress-step project-progress-step-3 ${status3}">
                <hr />
                <div class="project-progress-circle">3</div>
                <div class="project-progress-circle-text">Completato / Operativo</div>
            </div>
        </div>
        <div class="project-data-table-container">
            <a class="btn btn-link project-data-legenda-button">Legenda</a>
            <div class="project-data-legenda"></div>
            <table class="project-data-table">
                <tr>
                    <th style="background-color:${colorRgba}">Destinatari</th>
                    <td>
                        <div class="project-data" style="background-color:${colorRgba}">
                            <i class="smart-citizen"></i>
                            <i class="smart-pa"></i>
                            <i class="smart-pa-plus"></i>
                        </div>
                    </td>
                </tr>
                <tr>
                    <th style="background-color:${colorRgba}">Scala di intervento</th>
                    <td>
                        <div class="project-data" style="background-color:${colorRgba}">
                            <i class="smart-territory-radius"></i>
                            <i class="smart-territory-radius"></i>
                        </div>
                    </td>
                </tr>
                <tr>
                    <th style="background-color:${colorRgba}">Sostenibilità</th>
                    <td>
                        <div class="project-data" style="background-color:${colorRgba}">
                            <i class="smart-territory-radius"></i>
                            <i class="smart-territory-radius"></i>
                        </div>
                    </td>
                </tr>
                <tr>
                    <th style="background-color:${colorRgba}">Valore complessivo</th>
                    <td>
                        <div class="project-data" style="background-color:${colorRgba}">
                            <i class="smart-territory-radius"></i>
                            <i class="smart-territory-radius"></i>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</div>
