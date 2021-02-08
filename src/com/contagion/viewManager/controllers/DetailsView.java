package com.contagion.viewManager.controllers;

import com.contagion.control.Storage;
import com.contagion.map.Map;
import com.contagion.person.Client;
import com.contagion.person.Person;
import com.contagion.person.Supplier;
import com.contagion.shop.Product;
import com.contagion.shop.Shop;
import com.contagion.tiles.Drawable;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;


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
    private TableColumn<Client, String> clientNextShopCol;
    @FXML
    private TableColumn<Client, Double> clientCartOccupancyCol;
    @FXML
    private TableColumn<Client, Boolean> clientIsSickCol;
    @FXML
    private TableColumn<Person, CheckBox> clientIsMaskedCol;
    @FXML
    private TableColumn<Person, CheckBox> clientIsVaccinatedCol;

    @FXML
    private TableView<Supplier> supplierTableView;
    @FXML
    private TableColumn<Supplier, String> supplierNameCol;
    @FXML
    private TableColumn<Supplier, String> supplierSurnameCol;
    @FXML
    private TableColumn<Supplier, String> supplierCompanyName;
    @FXML
    private TableColumn<Supplier, String> supplierIdCol;
    @FXML
    private TableColumn<Supplier, Double> supplierTankOccupancyCol;
    @FXML
    private TableColumn<Supplier, Double> supplierTrunkOccupancyCol;
    @FXML
    private TableColumn<Supplier, String> supplierIsSickCol;
    @FXML
    private TableColumn<Person, CheckBox> supplierIsMaskedCol;
    @FXML
    private TableColumn<Person, CheckBox> supplierIsVaccinatedCol;

    @FXML
    private TableView<Shop> retailShopTableView;
    @FXML
    private TableColumn<Shop, String> retailShopNameCol;
    @FXML
    private TableColumn<Shop, String> retailShopIdCol;
    @FXML
    private TableColumn<Shop, Double> retailShopSupplyOccupancyCol;

    @FXML
    private TableView<Shop> wholesaleTableView;
    @FXML
    private TableColumn<Shop, String> wholesaleNameCol;
    @FXML
    private TableColumn<Shop, String> wholesaleIdCol;
    @FXML
    private TableColumn<Shop, Double> wholesaleSupplyOccupancyCol;

    @FXML
    private TableColumn removeClient;
    @FXML
    private TableColumn removeSupplier;

    @FXML
    private TableView<Product> productTableView;
    @FXML
    private TableColumn<Product, String> productNameCol;
    @FXML
    private TableColumn<Product, String> productCompanyNameCol;
    @FXML
    private TableColumn<Product, String> productIdCol;
    @FXML
    private TableColumn<Product, String> productBBDateCol;
    @FXML
    private TableColumn<Product, String> productFromCol;
    @FXML
    private TableColumn<Product, String> productToCol;
    @FXML
    private TableColumn<Product, String> productDeliveredByCol;
    @FXML
    private TableColumn<Product, String> productBoughtByCol;
    @FXML
    private TableColumn<Product, Boolean> productExistsCol;
    @FXML
    private TableColumn<Product, Boolean> productDisposedCol;


    private void initializeClientTableView() {
        clientNameCol.setCellValueFactory(o -> new ReadOnlyStringWrapper(o.getValue().getName()));
        clientSurnameCol.setCellValueFactory(o -> new ReadOnlyStringWrapper(o.getValue().getSurname()));
        clientIdCol.setCellValueFactory(o -> new ReadOnlyStringWrapper(o.getValue().getId().toString()));
        clientNextShopCol.setCellValueFactory(new PropertyValueFactory<>("nextShop"));

        clientCartOccupancyCol.setCellValueFactory(new PropertyValueFactory<>("cartOccupancy"));
        clientCartOccupancyCol.setCellFactory(ProgressBarTableCell.forTableColumn());

        clientIsSickCol.setCellValueFactory(new PropertyValueFactory<>("isSick"));

        isMaskedCheckBox(clientIsMaskedCol);
        isVaccinatedCheckBox(clientIsVaccinatedCol);

        removeClient.setCellValueFactory((Callback<TableColumn.CellDataFeatures, ObservableValue>) tc -> {
            Button button = new Button("Delete");
            button.setOnAction(actionEvent -> {
                if(tc.getValue() instanceof Client) {
                    ((Client) tc.getValue()).destroy();
                }
            });
            return new SimpleObjectProperty(button);
        });

        clientTableView.setRowFactory(tc -> rowClickControl());
        clientTableView.setItems(Storage.INSTANCE.getClients());
    }

    private void isMaskedCheckBox(TableColumn<Person, CheckBox> col) {
        col.setCellValueFactory(arg -> {
            Person entity = arg.getValue();
            CheckBox checkBox = new CheckBox();
            checkBox.selectedProperty().setValue(entity.isMasked());
            checkBox.selectedProperty().addListener((observableValue, aBoolean, t1) -> entity.isMaskedProperty().set(t1));
            return new SimpleObjectProperty<>(checkBox);
        });
    }

    private void isVaccinatedCheckBox(TableColumn<Person, CheckBox> col) {
        col.setCellValueFactory(arg -> {
            Person entity = arg.getValue();
            CheckBox checkBox = new CheckBox();
            checkBox.selectedProperty().setValue(entity.isVaccinated());
            checkBox.selectedProperty().addListener((observableValue, aBoolean, t1) -> entity.isVaccinatedProperty().set(t1));
            return new SimpleObjectProperty<>(checkBox);
        });
    }

    private void initializeSupplierTableView() {
        supplierNameCol.setCellValueFactory(o -> new ReadOnlyStringWrapper(o.getValue().getName()));
        supplierSurnameCol.setCellValueFactory(o -> new ReadOnlyStringWrapper(o.getValue().getSurname()));
        supplierCompanyName.setCellValueFactory(o -> new ReadOnlyStringWrapper(o.getValue().getCompanyName()));
        supplierIdCol.setCellValueFactory(o -> new ReadOnlyStringWrapper(o.getValue().getId().toString()));

        supplierTankOccupancyCol.setCellValueFactory(new PropertyValueFactory<>("tankOccupancy"));
        supplierTankOccupancyCol.setCellFactory(ProgressBarTableCell.forTableColumn());

        supplierTrunkOccupancyCol.setCellValueFactory(new PropertyValueFactory<>("trunkOccupancy"));
        supplierTrunkOccupancyCol.setCellFactory(ProgressBarTableCell.forTableColumn());

        supplierIsSickCol.setCellValueFactory(new PropertyValueFactory<>("isSick"));

        isMaskedCheckBox(supplierIsMaskedCol);
        isVaccinatedCheckBox(supplierIsVaccinatedCol);

        removeSupplier.setCellValueFactory((Callback<TableColumn.CellDataFeatures, ObservableValue>) tc -> {
            Button button = new Button("Delete");
            button.setOnAction(actionEvent -> {
                if (tc.getValue() instanceof Supplier) {
                    ((Supplier) tc.getValue()).destroy();
                }
            });
            return new SimpleObjectProperty(button);
        });

        supplierTableView.setRowFactory(tc -> rowClickControl());
        supplierTableView.setItems(Storage.INSTANCE.getSuppliers());
    }

    private void initializeShop(TableView<Shop> tv, TableColumn<Shop, String> tc1, TableColumn<Shop, String> tc2, TableColumn<Shop, Double> tc3, ObservableList items) {
        tc1.setCellValueFactory(o -> new ReadOnlyStringWrapper(o.getValue().getName()));
        tc2.setCellValueFactory(o -> new ReadOnlyStringWrapper(o.getValue().getId().toString()));

        tc3.setCellValueFactory(new PropertyValueFactory<>("supplyOccupancy"));
        tc3.setCellFactory(ProgressBarTableCell.forTableColumn());

        tv.setRowFactory(tc -> rowClickControl());
        tv.setItems(items);
    }

    private void initializeProductsTableView() {
        productNameCol.setCellValueFactory(o -> new ReadOnlyStringWrapper(o.getValue().getName()));
        productCompanyNameCol.setCellValueFactory(o -> new ReadOnlyStringWrapper(o.getValue().getCompanyName()));
        productIdCol.setCellValueFactory(o -> new ReadOnlyStringWrapper(o.getValue().getId()));
        productBBDateCol.setCellValueFactory(o -> new ReadOnlyStringWrapper(o.getValue().getBbdate()));
        productFromCol.setCellValueFactory(o -> new ReadOnlyStringWrapper(o.getValue().getFrom()));

        productToCol.setCellValueFactory(new PropertyValueFactory<>("to"));
        productDeliveredByCol.setCellValueFactory(new PropertyValueFactory<>("deliveredBy"));
        productBoughtByCol.setCellValueFactory(new PropertyValueFactory<>("boughtBy"));
        productExistsCol.setCellValueFactory(new PropertyValueFactory<>("exists"));
        productDisposedCol.setCellValueFactory(new PropertyValueFactory<>("disposed"));


        productTableView.setItems(Storage.INSTANCE.getProducts());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeClientTableView();
        initializeSupplierTableView();
        initializeShop(retailShopTableView, retailShopNameCol, retailShopIdCol, retailShopSupplyOccupancyCol, Storage.INSTANCE.getRetailShops());
        initializeShop(wholesaleTableView, wholesaleNameCol, wholesaleIdCol, wholesaleSupplyOccupancyCol, Storage.INSTANCE.getWholesales());
        initializeProductsTableView();
    }

    private <T> TableRow<T> rowClickControl() {
        TableRow<T> row = new TableRow<>();
        row.setOnMouseClicked(event -> {
            if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY) {
                switch (event.getClickCount()) {
                    case 1 -> Map.getInstance().setTrackedObject((Drawable) row.getItem());
                    case 2 -> {
                        System.out.println(row.getItem());
                        displayGivenDetails();
                        Map.getInstance().setTrackedObject((Drawable) row.getItem());
                    }
                }
            }
        });
        return row;
    }

    private void displayGivenDetails() {
        //TODO
    }

}
