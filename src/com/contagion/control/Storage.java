package com.contagion.control;

import com.contagion.map.Position;
import com.contagion.pathfinding.Pathfinder;
import com.contagion.person.Client;
import com.contagion.person.Person;
import com.contagion.person.Supplier;
import com.contagion.shop.Product;
import com.contagion.shop.RetailShop;
import com.contagion.shop.Shop;
import com.contagion.shop.Wholesale;
import com.contagion.tiles.DrawableType;
import com.contagion.tiles.Movable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ScheduledFuture;

public enum Storage {
    INSTANCE;
    private final ObservableList clients = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());
    private final ObservableList suppliers = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());
    private final ObservableList products = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());
    private final HashMap<Person, ScheduledFuture<?>> personToFuture = new HashMap<>();
    private final ObservableList retailShops = FXCollections.observableArrayList();
    private final ObservableList wholesales = FXCollections.observableArrayList();
    private final ArrayList<Shop> allShops = new ArrayList<>();
    private final HashMap<Position, Shop> locationToShop = new HashMap<>();
    private final List<Position> clientPossibleSpawnPoints = new ArrayList<>();
    private final List<Position> supplierPossibleSpawnPoints = new ArrayList<>();
    private int longestPath;
    private boolean productsOverflow = true;

    private static final int MAX_NO_CLIENTS = 100;
    private static final int MAX_NO_SUPPLIERS = 100;
    private static final int MAX_NO_PRODUCTS = 10000;

    private final List<String> lastNames = Arrays.asList(
            "Smith",
            "Johnson",
            "Williams",
            "Brown",
            "Jones",
            "Miller",
            "Davis",
            "Garcia",
            "Rodriguez",
            "Wilson",
            "Martinez",
            "Anderson",
            "Taylor",
            "Thomas",
            "Hernandez",
            "Moore",
            "Martin",
            "Jackson",
            "Thompson",
            "White",
            "Lopez",
            "Lee",
            "Gonzalez",
            "Harris",
            "Clark",
            "Lewis",
            "Robinson",
            "Walker",
            "Perez",
            "Hall"
    );
    private final List<String> firstNames = Arrays.asList(
            "Michael",
            "James",
            "John",
            "Robert",
            "David",
            "William",
            "Mary",
            "Christopher",
            "Joseph",
            "Richard",
            "Daniel",
            "Thomas",
            "Matthew",
            "Jennifer",
            "Charles",
            "Anthony",
            "Patricia",
            "Linda",
            "Mark",
            "Elizabeth",
            "Joshua",
            "Steven",
            "Andrew",
            "Kevin",
            "Brian",
            "Barbara",
            "Jessica",
            "Jason",
            "Susan",
            "Timothy"
    );
    private final List<String> deliveryCompanyNames = Arrays.asList(
            "A Plus Carriers",
            "A Town Transportation",
            "Advanced Messenger Services",
            "Aero Speed Expedited Delivery",
            "Alves Delivery",
            "American Expediting",
            "Best Courier and Delivery Service",
            "Blaze Express Courier",
            "Business Express Courier Service",
            "CPR Courier",
            "Courier Service Inc.",
            "Delicious Planet Delivery",
            "Elite Delivery Service",
            "Excel Courier and Logistics",
            "Exel Direct",
            "Expressit",
            "Finish Line Delivery Courier",
            "Flagship Delivery",
            "Forward Motion Delivery Services",
            "Grand Courier Service",
            "Jem Delivery Services",
            "Mercury Delivery Services",
            "National Delivery Systems",
            "OnTrac",
            "Priority Dispatch",
            "Rapid Express Courier System",
            "Sosa Delivers",
            "Superior Delivery Service",
            "Viking Delivery Service",
            "Worldwide Express"
    );

    private final List<String> productNames = Arrays.asList(
            "Abilify",
            "Humira",
            "Nexium",
            "Crestor",
            "Enbrel",
            "Advair Diskus, Seretide",
            "Remicade",
            "Lantus Solostar",
            "Neulasta",
            "Copaxone",
            "Rituxan, MabThera",
            "Spiriva",
            "Januvia",
            "Lantus",
            "Atripla",
            "Cymbalta",
            "Avastin",
            "Lyrica",
            "OxyContin",
            "Celebrex",
            "Epogen",
            "Truvada",
            "Diovan",
            "Levemir",
            "Gleevec, Glivec",
            "Herceptin",
            "Vyvanse",
            "Lucentis",
            "Zetia",
            "Combivent",
            "Symbicort",
            "Namenda",
            "NovoLog FlexPen",
            "Xarelto",
            "NovoLog",
            "Humalog",
            "Suboxone",
            "Viagra",
            "Seroquel XR",
            "Incivo",
            "AndroGel",
            "Enoxaparin",
            "Ritalin",
            "ProAir HFA",
            "Alimta",
            "Victoza",
            "Synagis",
            "Avonex",
            "Renvela",
            "Rebif",
            "Cialis",
            "Gilenya",
            "Nasonex",
            "Stelara",
            "Restasis",
            "Budesonide",
            "Acetaminophen/hydrocodone",
            "Flovent HFA",
            "Lovaza",
            "Prezista",
            "Isentress",
            "Janumet",
            "Procrit, Eprex",
            "Doxycycline",
            "Orencia",
            "Amphetamine/dextroamphetamine",
            "Vesicare",
            "Dexilant",
            "Humalog KwikPen",
            "Neupogen",
            "Lidocaine",
            "Lunesta",
            "Fenofibrate",
            "Zytiga",
            "Reyataz",
            "Sensipar",
            "Metoprolol",
            "AcipHex",
            "Synthroid",
            "Avonex Pen",
            "Prevnar 13",
            "Xolair",
            "Lipitor",
            "levothyroxine",
            "Benicar",
            "Stribild",
            "Zostavax",
            "Pradaxa",
            "Vytorin",
            "Tamiflu",
            "Xgeva",
            "Evista",
            "Xeloda",
            "Aranesp",
            "Ventolin HFA",
            "divalproex sodium",
            "Afinitor",
            "Betaseron, Betaferon",
            "Adderall XR",
            "Complera"
    );

    private final List<String> productCompanyName = Arrays.asList(
            "AbbVie",
            "Allergan, Inc, Lilly Icos",
            "Amgen",
            "Amgen Inc.",
            "Astellas Pharma US",
            "AstraZeneca",
            "AstraZeneca, Shionogi",
            "Bayer",
            "Biogen Idec",
            "Biogen Idec, Chugai Pharmaceutical, Genentech/Roche",
            "Biogen Idec, Forest Laboratories",
            "Boehringer Ingelheim",
            "Boehringer Ingelheim, Lilly Icos",
            "Bristol-Myers Squibb",
            "Bristol-Myers Squibb Company",
            "Centocor Ortho Biotech, Inc., Mitsubishi Tanabe Pharma",
            "Chugai Pharmaceutical, Genentech/Roche",
            "Chugai Pharmaceutical, Hetero Drugs, Roche",
            "Daiichi Sankyo",
            "Eisai, Merck & Co.",
            "Eli Lilly and Company",
            "Eli Lilly and Company, Chugai Pharmaceutical",
            "Eli Lilly and Company, Lilly Icos",
            "Genentech",
            "Genentech, Novartis",
            "Genentech/Roche",
            "Generic",
            "Gilead Sciences",
            "Gilead Sciences, Inc.",
            "GlaxoSmithKline",
            "Janssen Biotech, Inc.",
            "Janssen Pharmaceuticals, Inc, Johnson & Johnson",
            "Johnson & Johnson",
            "MedImmune",
            "Merck & Co., Inc.",
            "Merck & Co., Schering-Plough",
            "Novartis",
            "Novartis Corporation",
            "Novo Nordisk",
            "Pfizer",
            "Reckitt Benckiser Pharmaceuticals Inc.",
            "Sanofi-Aventis",
            "Schering-Plough",
            "Serono",
            "Shire",
            "Takeda Pharmaceuticals North America, Inc",
            "Teva Pharmaceuticals"
    );

    private final List<String> shopNames = Arrays.asList(
            "Walmart",
            "Costco",
            "Kroger",
            "Amazon",
            "Schwarz Gruppe",
            "The Home Depot",
            "Walgreens Boots Alliance",
            "Aldi",
            "CVS Health",
            "Tesco",
            "Ahold Delhaize",
            "Target Corporation",
            "Æon",
            "Lowe's",
            "Albertsons"
    );

    public ObservableList<Client> getClients() {
        return clients;
    }

    public void removeClient(Movable entity) {
        clients.remove(entity);
    }

    public ObservableList<Supplier> getSuppliers() {
        return suppliers;
    }

    public void removeSupplier(Movable entity) {
        suppliers.remove(entity);
    }

    public ObservableList getProducts() {
        return products;
    }

    public ObservableList<RetailShop> getRetailShops() {
        return retailShops;
    }

    public ObservableList<Wholesale> getWholesales() {
        return wholesales;
    }

    public ArrayList<Shop> getAllShops() {
        return allShops;
    }

    public Shop getShopOnPosition(Position position) {
        return locationToShop.get(position);
    }

    public void addPersonToFutureMap(Person person, ScheduledFuture<?> future) {
        personToFuture.put(person, future);
    }

    public void removePersonToFutureMap(Person person) {
        personToFuture.remove(person).cancel(false);
    }

    public void addClientPossibleSpawnPoints(Position position) {
        clientPossibleSpawnPoints.add(position);
    }

    public void addSupplierPossibleSpawnPoints(Position position) {
        supplierPossibleSpawnPoints.add(position);
    }

    /**
     * Additional setup, pathfinding needs Map so it has to be invoked outside Map constructor
     * find longest path to set supplier max fuel level
     */
    public void findLongestPath() {
        int longestPath = 0;

        for (int i = 0; i < allShops.size(); i++) {
            for (int j = i + 1; j < allShops.size(); j++) {
                List<String> currentPath = Pathfinder.findPath(allShops.get(i).getPosition(), allShops.get(j).getPosition(), DrawableType.Supplier);
                if (currentPath == null) {
                    continue;
                }
                if (currentPath.size() > longestPath) {
                    longestPath = currentPath.size();
                }
            }
        }
        this.longestPath = longestPath;
    }

    public int getLongestPath() {
        return longestPath;
    }

    public void setAllShopsCapacity(boolean isLockdown) {
        if (isLockdown) {
            for (Shop shop : allShops) {
                shop.lockdown();
            }
        } else {
            for (Shop shop : allShops) {
                shop.endLockdown();
            }
        }
    }

    public int randomNumberGenerator(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Max must be greater or equal to min " + min + " " + max);
        }

        return (int) (Math.random() * (max - min + 1) + min);
    }

    public <T> T randomPick(List<T> list) {
        return list.get(randomNumberGenerator(0, list.size() - 1));
    }

    public <E> List<E> sample(List<E> list, int n) {
        int length = list.size();
        Random random = new Random();

        if (length < n) {
            System.out.println("Sample size is larger than population, performing sample with repetition");
            List<E> sample = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                sample.add(list.get(randomNumberGenerator(0, list.size() - 1)));
            }
            return sample;
        }

        List<E> tmp = new ArrayList<>(list);
        for (int i = length - 1; i >= length - n; --i) {
            Collections.swap(tmp, i, random.nextInt(i + 1));
        }
        return tmp.subList(length - n, length);
    }

    public void createClient() {
        if (clients.size() < MAX_NO_CLIENTS) {
            clients.add(new Client(randomPick(firstNames), randomPick(lastNames), randomPick(clientPossibleSpawnPoints), randomNumberGenerator(5, 7), randomNumberGenerator(3, 5)));
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Why would you do that");
            alert.setContentText(String.format("Why would you need more than %d clients. Sorry but that's the limit", MAX_NO_CLIENTS));
            alert.showAndWait();
        }
    }

    public void createSupplier() {
        if (suppliers.size() < MAX_NO_SUPPLIERS) {
            List<Shop> shops = sample(wholesales, randomNumberGenerator(1, wholesales.size() - 1));
            shops.addAll(sample(retailShops, randomNumberGenerator(1, retailShops.size() - 1)));
            suppliers.add(new Supplier(randomPick(firstNames), randomPick(lastNames), randomPick(supplierPossibleSpawnPoints), randomNumberGenerator(5, 7), shops, randomPick(deliveryCompanyNames)));
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Why would you do that");
            alert.setContentText(String.format("Why would you need more than %d suppliers. Sorry but that's the limit", MAX_NO_SUPPLIERS));
            alert.showAndWait();
        }

    }

    public Product createProduct(String from) {
        Product product = new Product(randomPick(productNames), randomPick(productCompanyName), LocalDate.now().plusWeeks(randomNumberGenerator(24, 60)), from);
        //adding up to 10_000 products to the list (program will go on but future products won't be loges)
        if (products.size() < MAX_NO_PRODUCTS) {
            products.add(product);
        } else {
            if (productsOverflow) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("NO MORE");
                alert.setContentText(String.format("No more products will we added to details view, that %d should be enough", MAX_NO_PRODUCTS));
                alert.showAndWait();
                productsOverflow = false;
            }
        }
        return product;
    }

    public void createRetailShop(Position position) {
        RetailShop retailShop = new RetailShop(randomPick(shopNames), position, randomNumberGenerator(3, 7), randomNumberGenerator(20, 40));
        retailShops.add(retailShop);
        allShops.add(retailShop);
        locationToShop.put(position, retailShop);
    }

    public void createWholesale(Position position) {
        Wholesale wholesale = new Wholesale(randomPick(shopNames), position, randomNumberGenerator(3, 7), randomNumberGenerator(50, 100));
        wholesales.add(wholesale);
        allShops.add(wholesale);
        locationToShop.put(position, wholesale);
    }
}
