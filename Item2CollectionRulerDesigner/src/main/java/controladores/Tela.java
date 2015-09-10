package controladores;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import util.UtilidadesXML;
import br.unesp.repositorio.base.xmlschema.item2collectionruler.Campus;
import br.unesp.repositorio.base.xmlschema.item2collectionruler.Department;
import br.unesp.repositorio.base.xmlschema.item2collectionruler.Departments;
import br.unesp.repositorio.base.xmlschema.item2collectionruler.Rules;
import br.unesp.repositorio.base.xmlschema.item2collectionruler.University;
import br.unesp.repositorio.tools.item2collectionruler.tools.TextUtils;

public class Tela {

	private University university;

	@FXML
	ListView<Campus> listaCampi;

	@FXML
	ListView<Department> listaDepartamentos;

	@FXML
	ListView<String> listaRegrasCampus;

	@FXML
	ListView<String> listaRegrasDepartamento;

	@FXML
	TextField txtRegraCampus;

	@FXML
	TextField txtRegraDepartamento;

	@FXML
	TextField txtRotuloCampus;

	@FXML
	TextField txtHandleCampus;

	@FXML
	TextField txtRotuloDepartamento;

	@FXML
	TextField txtHandleDepartamento;

	@FXML
	TextArea visualizadorXML;

	private ObservableList<String> ovListaRegrasCampus;
	private ObservableList<String> ovListaRegraDepartamento;
	private ObservableList<Department> ovListaDepartamentos;

	@FXML
	private TextField txtDepartamento;

	public Tela() {
		novoXML();

	}

	public void novoXML() {
		university = new University();
		atualizaJanela();
	}

	public void abrirXML(){
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileNameExtensionFilter("Abrir xml", "xml"));
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setAcceptAllFileFilterUsed(true);
		fc.showDialog(null, "Select");
		if(fc.getSelectedFile()!=null){
			try {
				university = UtilidadesXML.carregaXML(fc.getSelectedFile().getAbsolutePath());
				atualizaEditorTexto();
				atualizaEditorVisual();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void salvarXML(){
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileNameExtensionFilter("Salvar xml", "xml"));
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setAcceptAllFileFilterUsed(true);
		fc.showSaveDialog(null);
		if(fc.getSelectedFile()!=null){
			try {
				UtilidadesXML.toXML(university,fc.getSelectedFile());
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Erro ao salvar arquivo","Erro",JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void sair(){
		System.exit(0);
	}

	public void adicionarCampus(){
		String rotulo = JOptionPane.showInputDialog("Rótulo da novo Campus");
		if(rotulo!=null && !rotulo.isEmpty()){
			Campus campus = new Campus();
			campus.setLabel(rotulo);
			campus.setHandle(rotulo);
			campus.setRules(new Rules());
			university.getCampi().getCampus().add(campus);
			atualizaJanela();
			listaCampi.getFocusModel().focus(university.getCampi().getCampus().indexOf(campus));
		}
	}

	public void removerCampus(){
		Campus campus = listaCampi.getFocusModel().getFocusedItem();
		if(campus!=null){

			ovListaRegrasCampus.remove(campus);
			university.getCampi().getCampus().remove(campus);
			atualizaJanela();
		}
	}

	private void atualizaJanela() {

		atualizaEditorVisual();
		atualizaEditorTexto();

	}

	public void adicionarRegraCampus(){
		String regra = txtRegraCampus.getText();
		if(regra!=null && !regra.isEmpty()){
			regra = TextUtils.removeExtraSpaces(TextUtils.removePuncts(TextUtils.removeAccents(regra.trim().toLowerCase())));
			Campus campus = listaCampi.getFocusModel().getFocusedItem();
			if(campus!=null){
				campus.getRules().getMatch().add(regra);
				atualizaJanela();
				listaCampi.getFocusModel().focus(university.getCampi().getCampus().indexOf(campus));
			}

		}
	}

	public void removerRegraCampus(){
		String regra = listaRegrasCampus.getFocusModel().getFocusedItem();
		if(regra!=null && !regra.isEmpty()){
			Campus campus = listaCampi.getFocusModel().getFocusedItem();
			if(campus!=null){
				campus.getRules().getMatch().remove(regra);
				atualizaJanela();
				listaCampi.getFocusModel().focus(university.getCampi().getCampus().indexOf(campus));
			}

		}
	}

	public void aplicarAlteracoesCampus(){
		Campus campus = listaCampi.getFocusModel().getFocusedItem();
		if(campus==null)return;
		if(txtHandleCampus.getText()!=null && !txtHandleCampus.getText().isEmpty()){
			if(campus.getDepartments()!=null && !campus.getDepartments().getDepartment().isEmpty()){
				if(JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(null, "Este campus já tem departamentos. Deseja remover os departamentos para adicionar o handle?")){
					return;
				}
			}
			campus.setDepartments(null);
			atualizaDepartamentos(campus);

			campus.setHandle(txtHandleCampus.getText());
		}
		campus.setLabel(txtRotuloCampus.getText());
		atualizaJanela();
	}

	private void atualizaEditorTexto(){
		try{
			visualizadorXML.setText(UtilidadesXML.toXML(university));

		}catch(Exception e){

		}
	}

	private void atualizaEditorVisual(){
		try{
			txtRegraCampus.setText("");
			txtRegraDepartamento.setText("");
			txtRotuloCampus.setText("");
			txtRotuloDepartamento.setText("");
			txtHandleCampus.setText("");
			txtHandleDepartamento.setText("");
			txtDepartamento.setText("");
			
			Campus campus = listaCampi.getFocusModel().getFocusedItem();
			Department departamento = listaDepartamentos.getFocusModel().getFocusedItem();
			
			listaCampi.getItems().clear();
			listaDepartamentos.getItems().clear();
			listaRegrasCampus.getItems().clear();
			listaRegrasDepartamento.getItems().clear();
			
			ObservableList<Campus> lista = FXCollections.observableArrayList(university.getCampi().getCampus());
			listaCampi.setItems(lista);
			
			listaCampi.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Campus>() {
				public void changed(ObservableValue<? extends Campus> ov,
						Campus antigo, Campus novo) {
					atualizaRegrasCampus(novo);
					atualizaDepartamentos(novo);
				}
			});
			listaDepartamentos.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Department>() {
				public void changed(ObservableValue<? extends Department> ov,
						Department antigo, Department novo) {
					atualizaRegrasDepartamento(novo);
				}
			});

			atualizaRegrasCampus(campus);
			atualizaDepartamentos(campus);
			atualizaRegrasDepartamento(departamento);
			
		}catch(Exception e){

		}
	}



	private void atualizaRegrasCampus(Campus campus) {
		if(campus!=null){
			ovListaRegrasCampus = FXCollections.observableArrayList(campus.getRules().getMatch());
			listaRegrasCampus.setItems(ovListaRegrasCampus);
			txtRotuloCampus.setText(campus.getLabel());
			txtHandleCampus.setText(campus.getHandle());
		}else{
			ovListaRegrasCampus = FXCollections.observableArrayList();
			listaRegrasCampus.setItems(ovListaRegrasCampus);
			txtRotuloCampus.setText("");
			txtHandleCampus.setText("");
		}
	}
	
	private void atualizaDepartamentos(Campus campus) {
		if(campus!=null && campus.getDepartments()!=null){
			ovListaDepartamentos = FXCollections.observableArrayList(campus.getDepartments().getDepartment());
			listaDepartamentos.setItems(ovListaDepartamentos);

		}else{
			ovListaDepartamentos = FXCollections.observableArrayList();
			listaDepartamentos.setItems(ovListaDepartamentos);

		}
	}

	private void atualizaRegrasDepartamento(Department departamento) {

		if(departamento!=null){
			ovListaRegraDepartamento = FXCollections.observableArrayList(departamento.getRules().getMatch());
			listaRegrasDepartamento.setItems(ovListaRegraDepartamento);
			txtRotuloDepartamento.setText(departamento.getLabel());
			txtHandleDepartamento.setText(departamento.getHandle());
		}else{
			ovListaRegraDepartamento = FXCollections.observableArrayList();
			listaRegrasDepartamento.setItems(ovListaRegraDepartamento);
			txtRotuloDepartamento.setText("");
			txtHandleDepartamento.setText("");
		}
	}

	public void adicionarDepartamento(){
		Campus campus = listaCampi.getFocusModel().getFocusedItem();
		if(campus==null)return;
		if(campus.getHandle()!=null && !campus.getHandle().isEmpty()){
			if(JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "Este campus já tem um handle padrão. Deseja remover o handle para adicionar o departamento?")){
				campus.setHandle(null);
				campus.setDepartments(new Departments());
			}else{
				return;
			}
		}
		Department departamento = new Department();
		departamento.setRules(new Rules());
		departamento.setLabel(txtDepartamento.getText());
		campus.getDepartments().getDepartment().add(departamento);
		atualizaJanela();
		listaCampi.getFocusModel().focus(university.getCampi().getCampus().indexOf(campus));
		listaDepartamentos.getFocusModel().focus(campus.getDepartments().getDepartment().indexOf(departamento));
	}

	public void removerDepartamento(){
		Campus campus = listaCampi.getFocusModel().getFocusedItem();
		if(campus!=null){
			Department departamento = listaDepartamentos.getFocusModel().getFocusedItem();
			if(departamento!=null){
				campus.getDepartments().getDepartment().remove(departamento);
				ovListaDepartamentos.remove(departamento);
				atualizaJanela();
				listaCampi.getFocusModel().focus(university.getCampi().getCampus().indexOf(campus));
				
			}

		}
	}

	public void adicionarRegraDepartamento(){
		Campus campus = listaCampi.getFocusModel().getFocusedItem();
		if(campus==null)return;

		String regra = txtRegraDepartamento.getText();
		if(regra!=null && !regra.isEmpty()){
			regra = TextUtils.removeExtraSpaces(TextUtils.removePuncts(TextUtils.removeAccents(regra.trim().toLowerCase())));

			Department departamento = listaDepartamentos.getFocusModel().getFocusedItem();
			if(departamento!=null){
				departamento.getRules().getMatch().add(regra);
				atualizaJanela();
				listaCampi.getFocusModel().focus(university.getCampi().getCampus().indexOf(campus));
				listaDepartamentos.getFocusModel().focus(campus.getDepartments().getDepartment().indexOf(departamento));
			}

		}
	}

	public void removerRegraDepartamento(){
		String regra = listaRegrasDepartamento.getFocusModel().getFocusedItem();
		if(regra!=null && !regra.isEmpty()){
			Campus campus = listaCampi.getFocusModel().getFocusedItem();
			Department departamento = listaDepartamentos.getFocusModel().getFocusedItem();
			if(departamento!=null){
				departamento.getRules().getMatch().remove(regra);
				atualizaJanela();
				listaCampi.getFocusModel().focus(university.getCampi().getCampus().indexOf(campus));
				listaDepartamentos.getFocusModel().focus(campus.getDepartments().getDepartment().indexOf(departamento));
			}

		}
	}

	public void aplicarAlteracoesDepartamento(){
		Department departamento = listaDepartamentos.getFocusModel().getFocusedItem();
		if(departamento!= null){
			departamento.setLabel(txtRotuloDepartamento.getText());
			departamento.setHandle(txtHandleDepartamento.getText());
		}
		atualizaJanela();
	}
}
