<#assign Validator = staticUtil["com.liferay.portal.kernel.util.Validator"] />
<#assign ACLSU = staticUtil["com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil"] />
<#assign TSCU = staticUtil["it.smartcommunitylab.tsc.utils.TscUtil"] />
<#assign colors = TSCU.getStructureFieldValues(siteGroupId, "TSC Category structure", "color", true) />
<#assign colorHex = colors[request.parameters.categoryId] />
<#assign colorRgba = TSCU.hex2rgba(colorHex, 0.5) />
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
            <a class="btn btn-link project-data-table-legenda-button"><span>Legenda</span></a>
            <div class="project-data-table-legenda">
                <div class="project-data-table-legenda-content">
                    <h5>Destinatari</h5>
                    <ul>
                        <li>
                            <i class="smart-citizen"></i>
                            <span>Cittadini: si intende la popolazione residente</span>
                        </li>
                        <li>
                            <i class="smart-city-user"></i>
                            <span>City user: si intende chi frequenta la città - in cui non è residente - per lavoro, per usufruire di servizi, per fare acquisti, per turismo ecc.</span>
                        </li>
                        <li>
                            <i class="smart-pa"></i>
                            <span>Struttura comunale</span>
                        </li>
                        <li>
                            <i class="smart-pa-plus"></i>
                            <span>Altre pubbliche amministrazioni</span>
                        </li>
                        <li>
                            <i class="smart-company"></i>
                            <span>Imprese</span>
                        </li>
                        <li>
                            <i class="smart-non-profit"></i>
                            <span>Organizzazioni terzo settore</span>
                        </li>
                    </ul>
                    <h5>Sostenibilità</h5>
                    <ul>
                        <li>
                            <i class="smart-social"></i>
                            <span>Sociale</span>
                        </li>
                        <li>
                            <i class="smart-environment"></i>
                            <span>Ambientale</span>
                        </li>
                        <li>
                            <i class="smart-economy"></i>
                            <span>Economica</span>
                        </li>
                    </ul>
                    <h5>Scala geografica</h5>
                    <ul>
                        <li>
                            <i class="smart-territory-radius"></i>
                            <span>Quartiere</span>
                        </li>
                        <li>
                            <i class="smart-territory-radius"></i>
                            <i class="smart-territory-radius"></i>
                            <span>Città</span>
                        </li>
                        <li>
                            <i class="smart-territory-radius"></i>
                            <i class="smart-territory-radius"></i>
                            <i class="smart-territory-radius"></i>
                            <span>Area metropolitana</span>
                        </li>
                        <li>
                            <i class="smart-territory-radius"></i>
                            <i class="smart-territory-radius"></i>
                            <i class="smart-territory-radius"></i>
                            <i class="smart-territory-radius"></i>
                            <span>Regione / Italia</span>
                        </li>
                    </ul>
                    <h5>Valore complessivo</h5>
                    <ul>
                        <li>
                            <i class="smart-price"></i>
                            <span>< 100K</span>
                        </li>
                        <li>
                            <i class="smart-price"></i>
                            <i class="smart-price"></i>
                            <span>100K - 500K</span>
                        </li>
                        <li>
                            <i class="smart-price"></i>
                            <i class="smart-price"></i>
                            <i class="smart-price"></i>
                            <span>500K - 1M</span>
                        </li>
                        <li>
                            <i class="smart-price"></i>
                            <i class="smart-price"></i>
                            <i class="smart-price"></i>
                            <i class="smart-price"></i>
                            <span>1M - 5M</span>
                        </li>
                        <li>
                            <i class="smart-price"></i>
                            <i class="smart-price"></i>
                            <i class="smart-price"></i>
                            <i class="smart-price"></i>
                            <i class="smart-price"></i>
                            <span>> 5M</span>
                        </li>
                    </ul>
                </div>
            </div>
            <table class="project-data-table">
                <tr>
                    <th style="background-color:${colorRgba}">Destinatari</th>
                    <td>
                        <div class="project-data" style="background-color:${colorRgba}">
                            <#--
                            <#assign siblings = destinatari.getSiblings() />
                            <ul>
                            <#list siblings as sibling>
                                <li>${sibling.getData()}</li>
                            </#list>
                            </ul>
                            -->
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
