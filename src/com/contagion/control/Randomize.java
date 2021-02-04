package com.contagion.control;

import com.contagion.map.Position;
import com.contagion.shop.Product;
import com.contagion.shop.RetailShop;
import com.contagion.shop.Wholesale;
import javafx.geometry.Pos;

import java.time.LocalDate;
import java.util.*;

public enum Randomize {
    INSTANCE;
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


    public int randomNumberGenerator(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Max must be greater or equal to min " + min + " " + max);
        }

        return (int) (Math.random() * (max - min + 1) + min);
    }

    public String randomPick(List<String> list) {
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

        for (int i = length - 1; i >= length - n; --i) {
            Collections.swap(list, i, random.nextInt(i + 1));
        }
        return list.subList(length - n, length);
    }

    public String getRandomFirstName() {
        return firstNames.get(randomNumberGenerator(0, firstNames.size() - 1));
    }

    public String getRandomLastName() {
        return lastNames.get(randomNumberGenerator(0, lastNames.size() - 1));
    }

    public Product createProduct() {
        return new Product(randomPick(productNames), randomPick(productCompanyName), LocalDate.now().plusWeeks(randomNumberGenerator(24, 60)));
    }

    public RetailShop createRetailShop(Position position) {
        return new RetailShop(randomPick(shopNames), position, randomNumberGenerator(3, 7), randomNumberGenerator(5, 10));
    }

    public Wholesale createWholesale(Position position){
        return new Wholesale(randomPick(shopNames), position, randomNumberGenerator(3, 7), randomNumberGenerator(20, 50));
    }

}
