package it.smartcommunitylab.platform.innoboard.converter;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import it.smartcommunitylab.platform.innoboard.converter.model.Project;
import it.smartcommunitylab.platform.innoboard.converter.model.RowValues;

public class XLSConverter {

	private static final String X = "x";
	
	private Properties mapping;
	private Properties implicitCategories;

	private String[] ambitiArray = new String[] { "finanziamentoPubblico", "ambitoPrimario", "ambitoSecondario" };

	public XLSConverter() throws Exception {
		mapping = new Properties();
		implicitCategories = new Properties();
		mapping.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("conversion/mapping.properties"));
		implicitCategories.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("conversion/implicitCategories.properties"));
	}

	public List<RowValues> readExcel(InputStream inp) throws Exception {
		XSSFWorkbook wb = new XSSFWorkbook(inp);
		XSSFExcelExtractor extractor = new XSSFExcelExtractor(wb);

		extractor.setFormulasNotResults(true);
		extractor.setIncludeSheetNames(false);

		Sheet sheet = wb.getSheetAt(0);

		List<RowValues> rowValuesList = Lists.newArrayList();
		HashMultiset<String> namesDups = HashMultiset.create();

		List<Integer> merged = Lists.newArrayList();
		for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			for (int j = range.getFirstRow(); j <= range.getLastRow(); j++) {
				merged.add(j);
			}
		}

		boolean bold = false;
		boolean italic = false;
		RowValues rowValues = null;
		RowValues parent = null;
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			XSSFRow row = (XSSFRow) sheet.getRow(i);

			if (merged.contains(i)) {
				continue;
			}
			if (row.getCell(1).toString().isEmpty()) {
				break;
			}
			if (row.getZeroHeight()) {
				continue;
			}

			if ("NO".equals(row.getCell(0).toString())) {
				continue;
			}

			bold = row.getCell(1).getCellStyle().getFont().getBold();
			italic = row.getCell(1).getCellStyle().getFont().getItalic();
			String name = row.getCell(1).getStringCellValue();

			rowValues = new RowValues();
			boolean upper = false;
			if (bold) {
				if (name.equals(name.toUpperCase())) {
					upper = true;
				}
				parent = rowValues;
			}
			if (italic && parent != null) {
				parent.getChildren().add(rowValues);
			}

			if (parent != null && parent.getChildren().contains(rowValues)) {
				System.out.print("\t");
			}

			rowValues.setName(name);
			namesDups.add(name);
			if (bold && !upper) {
				rowValues.setDupIndex(0);
			} else {
				rowValues.setDupIndex(namesDups.count(name));
			}

			for (int j = 2; j < row.getLastCellNum(); j++) {
				XSSFCell cell = (XSSFCell) row.getCell(j);
				if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
					rowValues.getValues().add(cell.getStringCellValue().trim());
				} else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
					rowValues.getValues().add(String.format("%.0f", cell.getNumericCellValue()));
				} else if (cell.getCellType() == XSSFCell.CELL_TYPE_FORMULA) {
					String formula = cell.getCellFormula();
					int start = formula.indexOf("\"") + 1;
					int end = formula.indexOf("\",");
					formula = formula.substring(start, end);
					rowValues.getValues().add(formula.trim());
				} else if (cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
					rowValues.getValues().add("");
				}

			}

			rowValuesList.add(rowValues);
		}

		extractor.close();
		
		return rowValuesList;

	}

	public Map<String, Project> convert(List<RowValues> valuesList) throws Exception {
		int colsN = 0;
		Project projects[] = null;
		Class<?> clazz = Class.forName("it.smartcommunitylab.platform.innoboard.converter.model.Project");
		for (RowValues values : valuesList) {
			if ("Nome del Comune".equals(values.getName())) {
				colsN = values.getValues().size();
				projects = new Project[colsN];
				for (int i = 0; i < colsN; i++) {
					projects[i] = new Project();
				}
			}
			
			// Titolo del progetto... (mapping.properties)
			String key = values.getName().replace(" ", "");
			String value = mapping.getProperty(key);

			if (value != null) {
				for (int i = 0; i < values.getValues().size(); i++) {
					Method method = clazz.getMethod("set" + WordUtils.capitalize(value), String.class);
					method.invoke(projects[i], values.getValues().get(i));
				}
			}

			// Destinatari intervento... (implicitCategories.properties)
			String sub = null;
			for (Object begin : implicitCategories.keySet()) {
				if (values.getName().replace(" ", "").startsWith(begin.toString())) {
					sub = implicitCategories.getProperty(begin.toString());
				}
			}

			if (sub != null) {
				for (int i = 0; i < values.getValues().size(); i++) {
					Method method = clazz.getMethod("get" + WordUtils.capitalize(sub));
					Map map = (Map) method.invoke(projects[i]);
					if (!values.getValues().get(i).isEmpty()) {
						map.put(extractType(values.getName()), (X.equals(values.getValues().get(i))?"":values.getValues().get(i)));
					}						
				}
			}

			if (!values.getChildren().isEmpty()) {
				if (values.getDupIndex() > 0) { // primari e secondari
					String ambito = ambitiArray[values.getDupIndex()];
					for (int i = 0; i < values.getValues().size(); i++) {
						Method method = clazz.getMethod("get" + WordUtils.capitalize(ambito));
						Map map = (Map) method.invoke(projects[i]);

						String name = values.getName();
						
						if (X.equals(values.getValues().get(i))) {
							if (ambito.contains("Primario") && map.keySet().size() == 1 && !map.containsKey(name)) {
								continue;
							}							
							
							List<String> old = (List<String>) map.get(name);
							if (old == null) {
								old = Lists.newArrayList();
							}
							map.put(name, old);
						}

						for (RowValues child : values.getChildren()) {
							if (X.equals(child.getValues().get(i))) {
								List<String> old = (List<String>) map.get(name);
								if (old == null) {
									old = Lists.newArrayList();
								}
								old.add(extractType(child.getName()));
								map.put(name, old);
							}
						}
					}
				} else { // finanziamenti
					String ambito = ambitiArray[values.getDupIndex()];
					for (int i = 0; i < values.getValues().size(); i++) {
						Method method = clazz.getMethod("get" + WordUtils.capitalize(ambito));
						Map map = (Map) method.invoke(projects[i]);	
						
						for (RowValues child : values.getChildren()) {
							if (!child.getValues().get(i).isEmpty()) {
								map.put(child.getName(), (X.equals(child.getValues().get(i))?"":child.getValues().get(i)));
							}
						}						
					}
				}
			}

		}

		Map<String, Project> result = Maps.newTreeMap();
		for (Project project: projects) {
			result.put(project.getTitolo(), project);
		}
		return result;
	}

	private String extractType(String string) {
		int start = string.indexOf("[") + 1;
		int end = string.indexOf("]");
		String result = string.substring(start, end);
		return result;
	}

	
}
