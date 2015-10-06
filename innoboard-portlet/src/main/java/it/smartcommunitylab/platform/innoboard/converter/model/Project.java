package it.smartcommunitylab.platform.innoboard.converter.model;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Project {

	private String comune; // Comune di Torino
	private String descrizione;
	private String titolo;
	private String paroleChiave;
	private String abstractProgetto;
	private String obiettivi; // OBIETTIVI
	private String fasi;
	private String valoreComplessivo;
	private String link;
	private String scalaGeografica;
	private String dataAvvio;
	private String statoAvanzamento;
	private String sostenibilitaSociale;
	private String sostenibilitaEconomica;
	private String sostenibilitaAmbientale;

	private List<String> tipologia;
	private List<String> destinatari;
	private List<String> tipoInnovazione;

	private Map<String, List<String>> ambitoPrimario;
	private Map<String, List<String>> ambitoSecondario;
	private Map<String, String> finanziamentoPubblico;

	public Project() {
		tipologia = Lists.newArrayList();
		destinatari = Lists.newArrayList();
		tipoInnovazione = Lists.newArrayList();
		ambitoPrimario = Maps.newTreeMap();
		ambitoSecondario = Maps.newTreeMap();
		finanziamentoPubblico = Maps.newTreeMap();
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getParoleChiave() {
		return paroleChiave;
	}

	public void setParoleChiave(String paroleChiave) {
		this.paroleChiave = paroleChiave;
	}

	public String getAbstractProgetto() {
		return abstractProgetto;
	}

	public void setAbstractProgetto(String abstractProgetto) {
		this.abstractProgetto = abstractProgetto;
	}

	public String getObiettivi() {
		return obiettivi;
	}

	public void setObiettivi(String obiettivi) {
		this.obiettivi = obiettivi;
	}

	public String getFasi() {
		return fasi;
	}

	public void setFasi(String fasi) {
		this.fasi = fasi;
	}

	public String getValoreComplessivo() {
		return valoreComplessivo;
	}

	public void setValoreComplessivo(String valoreComplessivo) {
		this.valoreComplessivo = valoreComplessivo;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getScalaGeografica() {
		return scalaGeografica;
	}

	public void setScalaGeografica(String scalaGeografica) {
		this.scalaGeografica = scalaGeografica;
	}

	public String getDataAvvio() {
		return dataAvvio;
	}

	public void setDataAvvio(String dataAvvio) {
		this.dataAvvio = dataAvvio;
	}

	public String getStatoAvanzamento() {
		return statoAvanzamento;
	}

	public void setStatoAvanzamento(String statoAvanzamento) {
		this.statoAvanzamento = statoAvanzamento;
	}

	public String getSostenibilitaSociale() {
		return sostenibilitaSociale;
	}

	public void setSostenibilitaSociale(String sostenibilitaSociale) {
		this.sostenibilitaSociale = sostenibilitaSociale;
	}

	public String getSostenibilitaEconomica() {
		return sostenibilitaEconomica;
	}

	public void setSostenibilitaEconomica(String sostenibilitaEconomica) {
		this.sostenibilitaEconomica = sostenibilitaEconomica;
	}

	public String getSostenibilitaAmbientale() {
		return sostenibilitaAmbientale;
	}

	public void setSostenibilitaAmbientale(String sostenibilitaAmbientale) {
		this.sostenibilitaAmbientale = sostenibilitaAmbientale;
	}

	public List<String> getTipologia() {
		return tipologia;
	}

	public void setTipologia(List<String> tipologia) {
		this.tipologia = tipologia;
	}

	public List<String> getDestinatari() {
		return destinatari;
	}

	public void setDestinatari(List<String> destinatari) {
		this.destinatari = destinatari;
	}

	public List<String> getTipoInnovazione() {
		return tipoInnovazione;
	}

	public void setTipoInnovazione(List<String> tipoInnovazione) {
		this.tipoInnovazione = tipoInnovazione;
	}

	public Map<String, List<String>> getAmbitoPrimario() {
		return ambitoPrimario;
	}

	public void setAmbitoPrimario(Map<String, List<String>> ambitoPrimario) {
		this.ambitoPrimario = ambitoPrimario;
	}

	public Map<String, List<String>> getAmbitoSecondario() {
		return ambitoSecondario;
	}

	public void setAmbitoSecondario(Map<String, List<String>> ambitoSecondario) {
		this.ambitoSecondario = ambitoSecondario;
	}

	public Map<String, String> getFinanziamentoPubblico() {
		return finanziamentoPubblico;
	}

	public void setFinanziamentoPubblico(Map<String, String> fonteFinanziamento) {
		this.finanziamentoPubblico = fonteFinanziamento;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
