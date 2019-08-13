package application;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class UserInfoController implements Initializable {

	@FXML Label lb_name;
	@FXML ListView lv_namelist, lv_info, lv_column;
	
	ObservableList observableList = null, listColumn = null;
	ResultSet rs = null;
	ResultSet rscolumn = null;
	
	String sql = "";
	String sqlResult = "";
	String sqlColumn = "";
	
	String[] columnName;
	int columnLength = 0;
	
	private Statement st;
	public void setStatement(Statement st) {
		this.st = st;
	

		try {
			observableList = FXCollections.observableArrayList();
			sql = "select name from tabledb.usertbl;";
			
			rs = st.executeQuery(sql);
			
			while(rs.next()) {
				sqlResult = rs.getString("name");
				observableList.add(sqlResult);
				lv_namelist.setItems(observableList);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		lv_namelist.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try {
					
					observableList = FXCollections.observableArrayList();
					listColumn = FXCollections.observableArrayList();
					
					lb_name.setText(newValue + "  ");
					
					sql = "show columns from tabledb.usertbl;";
					rscolumn = st.executeQuery(sql);
					
					rscolumn.last();
					columnLength = rscolumn.getRow();
					columnName = new String[columnLength];
					int i = 0;
					rscolumn.first();
					
					
					do {
						
						columnName[i] = rscolumn.getString("Field");
						
						listColumn.add(columnName[i]);
						lv_column.setItems(listColumn);
						
						
						i++;
						
					}while(rscolumn.next());
					
					
					rscolumn.close();
					
						
					sql = "select * from tabledb.usertbl where name=\"" + newValue + "\";";
					rs = st.executeQuery(sql);
					rs.first();
					 do{
						for(int j = 0; j < columnLength; j++) {
							sqlResult = rs.getString(columnName[j]);
							observableList.add(sqlResult);
							lv_info.setItems(observableList);
						}
						
						
					}while(rs.next());
						
					
				}catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

}
