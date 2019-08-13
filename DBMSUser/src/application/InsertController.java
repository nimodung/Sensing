package application;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class InsertController implements Initializable {

	@FXML TextField tf_sensingposition, tf_temperature, tf_sdate;
	@FXML Button btn_insert;
	@FXML ListView lv_sensing;
	
	ObservableList<String> observableList = null;
	ResultSet rs = null;
	
	String sqlResult = "";
	
	private Statement st;
	public void setStatement(Statement st) {
		this.st = st;
		setListView();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		

	}
	
	public void insertBtnHandler(ActionEvent event) {
		try {
			String sql = "";
			sql = "insert into tabledb.sensortbl (sensingposition, temperature, sdate) values(\'" 
				+ tf_sensingposition.getText() + "\', " + tf_temperature.getText() 
				+ ", \'" + tf_sdate.getText() + "\');";
		
			st.executeUpdate(sql);
			
			//setListView();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void setListView() {
		String sql = "";
		try {
			observableList = FXCollections.observableArrayList();
			sql = "describe tabledb.sensortbl;";
			rs = st.executeQuery(sql);
			rs.last();
			int columnindex = rs.getRow();
			String[] FieldList = new String[columnindex];
			int i = 0;
			do {
				FieldList[i] = rs.getString("Field");
				i++;
			}while(rs.next());
			
			
			
			sql = "select * from tabledb.sensortbl;";
			rs = st.executeQuery(sql);
			rs.first();
			do {
				for(int j = 0; j < columnindex; i++) {
					sqlResult = rs.getString(FieldList[j]);
					observableList.add(sqlResult);
					lv_sensing.setItems(observableList);
				}
			}while(rs.next());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return;
		
	}

}
