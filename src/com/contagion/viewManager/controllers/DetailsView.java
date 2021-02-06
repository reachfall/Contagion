package com.contagion.viewManager.controllers;

import com.contagion.control.Storage;
import com.contagion.person.Client;
import com.contagion.person.Supplier;
import com.contagion.shop.RetailShop;
import com.contagion.shop.Wholesale;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;


public class DetailsView implements Initializable {
    @FXML
    private TableView<Client> clientTableView;
    @FXML
    private TableColumn<Client, String> clientNameCol;
    @FXML
    private TableColumn<Client, String> clientSurnameCol;
    @FXML
    private TableColumn<Client, String> clientIdCol;
    @FXML
    private TableColumn<Client, Boolean> clientIsSick;
    @FXML
    private TableColumn<Client, Boolean> clientIsVaccinated;

    @FXML
    private TableView<Supplier> supplierTableView;
    @FXML
    private TableColumn<Supplier, String> supplierNameCol;
    @FXML
    private TableColumn<Supplier, String> supplierSurnameCol;
    @FXML
    private TableColumn<Supplier, String> supplierIdCol;
    @FXML
    private TableColumn<Supplier, String> supplierIsSick;
    @FXML
    private TableColumn<Supplier, String> supplierIsVaccinated;
    @FXML
    private TableColumn<Supplier, Integer> supplierFuelLevel;

    @FXML
    private TableView<RetailShop> retailShopTableView;
    @FXML
    private TableColumn<RetailShop, String> retailShopNameCol;
    @FXML
    private TableColumn<RetailShop, String> retailShopIdCol;

    @FXML
    private TableView<Wholesale> wholesaleTableView;
    @FXML
    private TableColumn<Wholesale, String> wholesaleNameCol;
    @FXML
    private TableColumn<Wholesale, String> wholesaleIdCol;


    private void initializeClientTableView() {
        clientNameCol.setCellValueFactory(o -> new ReadOnlyStringWrapper(o.getValue().getName()));
        clientSurnameCol.setCellValueFactory(o -> new ReadOnlyStringWrapper(o.getValue().getSurname()));
        clientIdCol.setCellValueFactory(o -> new ReadOnlyStringWrapper(o.getValue().getId().toString()));
        clientIsSick.setCellValueFactory(new PropertyValueFactory<Client, Boolean>("isSick"));
        clientIsVaccinated.setCellValueFactory(new PropertyValueFactory<Client, Boolean>("isVaccinated"));

        clientTableView.setItems(Storage.INSTANCE.getClients());
    }

    private void initializeSupplierTableView() {
        supplierNameCol.setCellValueFactory(o -> new ReadOnlyStringWrapper(o.getValue().getName()));
        supplierSurnameCol.setCellValueFactory(o -> new ReadOnlyStringWrapper(o.getValue().getSurname()));
        supplierIdCol.setCellValueFactory(o -> new ReadOnlyStringWrapper(o.getValue().getId().toString()));
        supplierIsSick.setCellValueFactory(new PropertyValueFactory<Supplier, String>("isSick"));
        supplierIsVaccinated.setCellValueFactory(new PropertyValueFactory<Supplier, String>("isVaccinated"));
        supplierFuelLevel.setCellValueFactory(new PropertyValueFactory<Supplier, Integer>("fuelLevel"));

        supplierTableView.setItems(Storage.INSTANCE.getSuppliers());
    }

    private void initializeRetailShopTableView() {
        retailShopNameCol.setCellValueFactory(o -> new ReadOnlyStringWrapper(o.getValue().getName()));
        retailShopIdCol.setCellValueFactory(o -> new ReadOnlyStringWrapper(o.getValue().getId().toString()));

        retailShopTableView.setItems(Storage.INSTANCE.getRetailShops());
    }

    private void initializeWholesaleTableView() {
        wholesaleNameCol.setCellValueFactory(o -> new ReadOnlyStringWrapper(o.getValue().getName()));
        wholesaleIdCol.setCellValueFactory(o -> new ReadOnlyStringWrapper(o.getValue().getId().toString()));

        wholesaleTableView.setItems(Storage.INSTANCE.getWholesales());
    }

    @FXML
    private void clickItem(MouseEvent event){
        if (event.getClickCount() == 1){
            System.out.println("clicked");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeClientTableView();
        initializeSupplierTableView();
        initializeRetailShopTableView();
        initializeWholesaleTableView();
    }
}
