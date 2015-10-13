<#assign TSCU = staticUtil["it.smartcommunitylab.tsc.utils.TscUtil"] />
<#assign Validator = staticUtil["com.liferay.portal.kernel.util.Validator"] />
<#assign ACLSU = staticUtil["com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil"] />
<#assign ambitoPrimarioJson = ambitoPrimario.getData()?eval />
<#assign ambitoPrimarioJsonKeys = ambitoPrimarioJson?keys />
<#assign ambitoPrimarioName = ambitoPrimarioJsonKeys[0] />
<#assign categoryId = stringUtil.valueOf(TSCU.getCategoryIdByName(stringUtil.valueOf(ambitoPrimarioName))) />

<#assign colors = TSCU.getStructureFieldValues(siteGroupId, "TSC Category structure", "color", true) />
<#assign colorHex = colors[categoryId] />
<#assign colorRgba = TSCU.hex2rgba(colorHex, 0.5) />

<#assign statoAvanzamento1 = (getterUtil.getNumber(statoAvanzamento.getData()) > 0)?string('done', '') />
<#assign statoAvanzamento2 = (getterUtil.getNumber(statoAvanzamento.getData()) > 1)?string('done', '') />
<#assign statoAvanzamento3 = (getterUtil.getNumber(statoAvanzamento.getData()) > 2)?string('done', '') />

<#assign scalaGeograficaIndex = 0 />
<#if scalaGeografica.getData() == 'Quartiere'>
	<#assign scalaGeograficaIndex = 1 />
<#elseif scalaGeografica.getData() == 'Comune'>
	<#assign scalaGeograficaIndex = 2 />
<#elseif scalaGeografica.getData() == 'Città metropolitana'>
	<#assign scalaGeograficaIndex = 3 />
<#elseif scalaGeografica.getData() == 'Rete di Comuni'>
	<#assign scalaGeograficaIndex = 4 />
<#elseif scalaGeografica.getData() == 'Provincia'>
	<#assign scalaGeograficaIndex = 5 />
<#elseif scalaGeografica.getData() == 'Regione'>
	<#assign scalaGeograficaIndex = 6 />
<#elseif scalaGeografica.getData() == 'Scala Nazionale'>
	<#assign scalaGeograficaIndex = 7 />
</#if>

<#assign valoreComplessivoIndex = 0 />
<#assign valoreComplessivoNumber = getterUtil.getNumber(valoreComplessivo.getData()) />
<#if (valoreComplessivoNumber < 100000)>
	<#assign valoreComplessivoIndex = 1 />
<#elseif (valoreComplessivoNumber >= 100000 && valoreComplessivoNumber < 500000)>
	<#assign valoreComplessivoIndex = 2 />
<#elseif (valoreComplessivoNumber >= 500000 && valoreComplessivoNumber < 1000000)>
	<#assign valoreComplessivoIndex = 3 />
<#elseif (valoreComplessivoNumber >= 1000000 && valoreComplessivoNumber < 5000000)>
	<#assign valoreComplessivoIndex = 4 />
<#elseif (valoreComplessivoNumber > 5000000)>
	<#assign valoreComplessivoIndex = 5 />
</#if>

<h1>${stringUtil.valueOf(.vars['reserved-article-title'].data)}</h1>
<p>PAROLE CHIAVE: ${paroleChiave.getData()}</p>
<div class="row-fluid">
    <div class="span6">
        <ul class="project-summary" style="border-color:${colorHex}">
            <li class="project-summary-title">
                in sintesi
            </li>
            <#if Validator.isNotNull(comune.getData())>
                <li class="project-summary-where">
                    <i class="smart-where"></i>
                    ${comune.getData()}
                </li>
            </#if>
            <#if Validator.isNotNull(tipologia.getData())>
                <li class="project-summary-what">
                    <i class="smart-what"></i>
                    <#assign tipologiaJson = tipologia.getData()?eval />
                    <#assign tipologiaKeys = tipologiaJson?keys />
                    <#assign tipologiaText = '' />
                    <#list tipologiaKeys as k>
                        <#assign tipologiaText = tipologiaText + k />
                        <#if (k != tipologiaKeys?last)>
                            <#assign tipologiaText = tipologiaText + ', ' />
                        </#if>
                    </#list>
                    <span>${tipologiaText}</span>
                </li>
            </#if>
            <li class="project-summary-category">
                <i class="smart-category"></i>
                <ul>
                    <li>
    	                <span>${ambitoPrimarioName}: </span>
    	                <#list ambitoPrimarioJson[ambitoPrimarioJsonKeys[0]] as value>
    	                	<span>${value}</span>
    	                </#list>
    	            </li>
                    <#assign ambitoSecondarioJson = ambitoSecondario.getData()?eval />
    	            <#list ambitoSecondarioJson?keys as k>
    	                <li>
    	                    <#assign asText = k />
    	                    <#if (ambitoSecondarioJson[k]?size > 0)>
    	                        <#assign asText = asText + ': ' />
    	                    </#if>
        	                <span>${asText}</span>
        	                <#assign subText = '' />
        	           	    <#list ambitoSecondarioJson[k] as sub>
        	           	        <#assign subText = subText + sub />
        	           		    <#if sub != ambitoSecondarioJson[k]?last>
        	           		        <#assign subText = subText + ', ' />
        	           		    </#if>
        	           		</#list>
        	           		<span>${subText}</span>
    	            	</li>
    	            </#list>
	            </ul>
            </li>
            <#if Validator.isNotNull(tipoInnovazione.getData())>
                <li class="project-summary-innovation">
                    <i class="smart-innovation"></i>
                    <#assign tipoInnovazioneJson = tipoInnovazione.getData()?eval />
                    <#assign tipoInnovazioneKeys = tipoInnovazioneJson?keys />
                    <#assign tipoInnovazioneText = '' />
                    <#list tipoInnovazioneKeys as k>
                    	<#assign tipoInnovazioneText = tipoInnovazioneText + k />
                    	<#if k != tipoInnovazioneKeys?last>
                    	    <#assign tipoInnovazioneText = tipoInnovazioneText + ', ' />
                    	</#if>
                    </#list>
                    ${tipoInnovazioneText}
                </li>
            </#if>
            <#if Validator.isNotNull(finanziamentoPubblico.getData())>
                <li class="project-summary-financing">
                    <i class="smart-financing"></i>
                    <#if getterUtil.getBoolean(finanziamentoPubblico.getData())>
                    	Finanziamento pubblico
                    <#else>
                    	Finanziamento privato
                    </#if>
                </li>
            </#if>
            <#if Validator.isNotNull(link.getData()?trim)>
                <#assign link = link.getData()?trim />
                <li class="project-summary-website">
                    <i class="smart-website"></i>
                    <a href="${link}">${link}</a>
                </li>
            </#if>
            <#--
            <#if Validator.isNotNull(project_masterplan.getData())>
                <li class="project-summary-masterplan">
                    <i class="smart-masterplan"></i>
                    <a href="${project_masterplan.getData()}">${project_website.getData()}</a>
                </li>
            </#if>
            -->
        </ul>
        <div class="project-description">
            <div>${abstractProgetto.getData()}</div>
            <#if Validator.isNotNull(obiettivi.getData()?trim)>
                <h5 class="title">Obiettivi:</h5>
                <div>${obiettivi.getData()?trim}</div>
            </#if>
            <#--
            <h4 class="title">Leggi tutto:</h4>
            <div>${fasi.getData()}</div>
            -->
        </div>
    </div>
    <div class="span6">
        <h6 class="project-update">Dettagli aggiornati al</h6>
        <h6 class="project-progress-title">Stato di avanzamento</h6>
        <div class="row-fluid project-progress-container">
            <div class="span4 project-progress-step project-progress-step-1 ${statoAvanzamento1}">
                <hr />
                <div class="project-progress-circle">1</div>
                <div class="project-progress-circle-text">Approvato e in attesa di avvio</div>
            </div>
            <div class="span4 project-progress-step project-progress-step-2 ${statoAvanzamento2}">
                <hr />
                <div class="project-progress-circle">2</div>
                <div class="project-progress-circle-text">Approvato e in sviluppo</div>
            </div>
            <div class="span4 project-progress-step project-progress-step-3 ${statoAvanzamento3}">
                <hr />
                <div class="project-progress-circle">3</div>
                <div class="project-progress-circle-text">Completato / Operativo</div>
            </div>
        </div>
        <div class="project-data-table-container">
        	<!-- LEGENDA -->
        	<script type="text/javascript">
        	    var variable = $('.project-data-table-legenda-button');
        	    variable.click(function() {
	            	$('.project-data-table-container').toggleClass('visible');
	            });
	            
	            function toggleLegenda () {
	                $('.project-data-table-container').toggleClass('visible');
	            }
        	</script>
            <a onclick="toggleLegenda()" class="btn btn-link project-data-table-legenda-button"><span>Legenda</span></a>
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
                        <li>
                            <i class="smart-other"></i>
                            <span>Altro (specificato)</span>
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
                        	<#list 1..2 as s>
                        		<i class="smart-territory-radius"></i>
                        	</#list>
                            <span>Comune</span>
                        </li>
                        <li>
                            <#list 1..3 as s>
                        		<i class="smart-territory-radius"></i>
                        	</#list>
                            <span>Città metropolitana</span>
                        </li>
                        <li>
                            <#list 1..4 as s>
                        		<i class="smart-territory-radius"></i>
                        	</#list>
                            <span>Rete di Comuni</span>
                        </li>
                        <li>
                            <#list 1..5 as s>
                        		<i class="smart-territory-radius"></i>
                        	</#list>
                            <span>Provincia</span>
                        </li>
                        <li>
                            <#list 1..6 as s>
                        		<i class="smart-territory-radius"></i>
                        	</#list>
                            <span>Regione</span>
                        </li>
                        <li>
                            <#list 1..7 as s>
                        		<i class="smart-territory-radius"></i>
                        	</#list>
                            <span>Scala Nazionale</span>
                        </li>
                    </ul>
                    <h5>Valore complessivo</h5>
                    <ul>
                        <li>
                            <i class="smart-price"></i>
                            <span>< 100K</span>
                        </li>
                        <li>
                            <#list 1..2 as v>
                        		<i class="smart-price"></i>
                        	</#list>
                            <span>100K - 500K</span>
                        </li>
                        <li>
                            <#list 1..3 as v>
                        		<i class="smart-price"></i>
                        	</#list>
                            <span>500K - 1M</span>
                        </li>
                        <li>
                            <#list 1..4 as v>
                        		<i class="smart-price"></i>
                        	</#list>
                            <span>1M - 5M</span>
                        </li>
                        <li>
                            <#list 1..5 as v>
                        		<i class="smart-price"></i>
                        	</#list>
                            <span>> 5M</span>
                        </li>
                    </ul>
                </div>
            </div>
            <!-- TABLE -->
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
                            <#assign djson = destinatari.getData()?eval />
                            <#assign dkeys = djson?keys />
                            <#list dkeys as k>
                            	<#if k == 'Struttura comunale'>
                            		<i class="smart-pa"></i>
                            	<#elseif k == 'Cittadini'>
                            		<i class="smart-citizen"></i>
                            	<#elseif k == 'City user'>
                            		<i class="smart-city-user"></i>
                            	<#elseif k == 'Imprese'>
                            		<i class="smart-company"></i>
                            	<#elseif k == 'Organizzazioni terzo settore'>
                            		<i class="smart-non-profit"></i>
                            	<#elseif k == 'Altre pubbliche amministrazioni'>
                            		<i class="smart-pa-plus"></i>
                            	<#else>
                            		<i class="smart-other"></i>
                            	</#if>
                            </#list>
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
                    <th style="background-color:${colorRgba}">Sostenibilità sociale</th>
                    <td>
                        <div class="project-data" style="background-color:${colorRgba}">
                            <#assign ssn = getterUtil.getNumber(sostenibilitaSociale.getData()) />
                        	<#list 1..ssn as s>
                        		<i class="smart-social"></i>
                        	</#list>
                        </div>
                    </td>
                </tr>
                <tr>
                    <th style="background-color:${colorRgba}">Sostenibilità ambientale</th>
                    <td>
                        <div class="project-data" style="background-color:${colorRgba}">
                            <#assign san = getterUtil.getNumber(sostenibilitaAmbientale.getData()) />
                            <#list 1..san as s>
                            	<i class="smart-environment"></i>
                        	</#list>
                        </div>
                    </td>
                </tr>
                <tr>
                    <th style="background-color:${colorRgba}">Sostenibilità economica</th>
                    <td>
                        <div class="project-data" style="background-color:${colorRgba}">
                            <#assign sen = getterUtil.getNumber(sostenibilitaEconomica.getData()) />
                            <#list 1..sen as s>
                            	<i class="smart-economy"></i>
                        	</#list>
                        </div>
                    </td>
                </tr>
                <tr>
                    <th style="background-color:${colorRgba}">Valore complessivo</th>
                    <td>
                        <div class="project-data" style="background-color:${colorRgba}">
                        	<#list 1..valoreComplessivoIndex as vci>
                        		<i class="smart-price"></i>
                        	</#list>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</div>

<#if (Validator.isNotNull(media) && (media.getSiblings()?size > 0) && (Validator.isNotNull(media.getSiblings()[0].getData())))>
    <#assign mediaList = media.getSiblings() />
    <div class="row-fluid">
        <div class="span12">
            <div class="project-media">
                <h4>Contenuti media</h4>
                <ul id="project-media-gallery">
                    <#list mediaList as m>
                        <li>
                            <img src="${m.getData()}" />
                        </li>
                    </#list>
                </ul>
            </div>
        </div>
    </div>
    
    <script type="text/javascript">
        $("#project-media-gallery").lightSlider({
            pager: false,
            adaptiveHeight: true,
            prevHtml: '<i class="icon-chevron-left"></i>',
            nextHtml: '<i class="icon-chevron-right"></i>'
        });
    </script>
</#if>