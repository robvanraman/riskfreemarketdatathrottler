package com.stocks.proj;
/**/
/**/
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;
import yahoofinance.quotes.fx.FxQuote;
import yahoofinance.quotes.fx.FxSymbols;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.spi.CalendarDataProvider;

import org.bson.Document;

import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.stocks.proj.RiskfreeMarketDataThrotler.Industry;
import com.stocks.proj.RiskfreeMarketDataThrotler.Size;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import com.stocks.model.*;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

import org.bson.Document;

public class RiskfreeMarketDataThrotler {

    enum Size {
        LOW,
        MEDIUM,
        HIGH
    }

    enum CAP {
        SMALL,
        MID,
        LARGE
    }

    enum Industry {
        FINANCE,
        TECHNOLOGY,
        HEALTHCARE,
        AUTO,
        ENERGY,
        REALESTATE,
        UTILITIES,
        INDUSTRY,
        STAPLES,
        MATERIALS

    }

    private static String connectionString = "mongodb://pe4:lSzxG3NNtxpNfeI4cqqs5cqOvoZqYRSM5upTL2oDJNyY6AJpYmTWvB5SImPzq4LxxrefU4ksZQoUACDbZyEgTg==@pe4.mongo.cosmos.azure.com:10255/?ssl=true&replicaSet=globaldb&retrywrites=false&maxIdleTimeMS=120000&appName=@pe4@";
    private static String databaseName = null;
    private static final String REDIS_HOSTNAME = "pe4.redis.cache.windows.net";
    private static final int REDIS_PORT = 6379;
    private static final String REDIS_PASSWORD = "at9xruoPnGtwKfpvgYvlz8iTsRUAvhNvVAzCaKJimio=";
    private static MongoClientURI uri = new MongoClientURI(connectionString);
    private static MongoClient mongoClient = new MongoClient(uri);
    private static String swing = null;
    private static Jedis jedis = null;
    private static JedisShardInfo shardInfo = null;


    private static MongoDatabase database = null;
    private static MongoCollection<Document> collection = null;

    public static void updateMongo(String security, String stockModel, StockDataModel dataModel) throws IOException {

        //String collectionName = "Security";
        // Set up the connection to the Azure Cosmos DB account
        //uri = new MongoClientURI(connectionString);
        //mongoClient = new MongoClient(uri);
        // MongoDatabase database = mongoClient.getDatabase(databaseName);
        //MongoCollection<Document> collection = database.getCollection(collectionName);


        Document filter = new Document("_id", security);
        // Size risk = getRisk();

        // Check if the document exists in the collection
        if (collection.countDocuments(filter) > 0) {

            // Create an update object with the new values
            Document update = new Document("$set", new Document("_id", security).append(security, stockModel)
                    .append("price", dataModel.getPrice())
                    .append("yeild", dataModel.getAnnualyeild())
                    .append("risk", dataModel.getRisk())
                    .append("sector", dataModel.getIndustry()));


            // Update the document in the collection
            UpdateResult result = collection.updateOne(filter, update);

            // Print the number of documents updated
            //   System.out.println(result.getModifiedCount());
        } else {

            // Create a new document to be inserted into the collection
            Document document = new Document("_id", security).append(security, stockModel);
            // Insert the document into the collection
            collection.insertOne(document);
        }

    }


    public static String getIndustry() {

        Random random = new Random();
        int randomNumber = random.nextInt(10);

        // Map the random number to an enum constant
        Industry industry;
        switch (randomNumber) {
            case 0:
                industry = Industry.AUTO;
                break;
            case 1:
                industry = Industry.ENERGY;
                break;
            case 2:
                industry = Industry.FINANCE;
                break;
            case 3:
                industry = Industry.HEALTHCARE;
                break;
            case 4:
                industry = Industry.INDUSTRY;
                break;
            case 5:
                industry = Industry.MATERIALS;
                break;

            case 6:
                industry = Industry.REALESTATE;
                break;
            case 7:
                industry = Industry.STAPLES;
                break;
            case 8:
                industry = Industry.TECHNOLOGY;
                break;
            case 9:
                industry = Industry.UTILITIES;
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + randomNumber);
        }
        return industry.toString();


    }

    public static String getRisk() {

        Random random = new Random();
        int randomNumber = random.nextInt(6);

        // Map the random number to an enum constant
        Size size;
        switch (randomNumber) {
            case 0:
                size = Size.LOW;
                break;
            case 1:
                size = Size.LOW;
                break;
            case 2:
                size = Size.MEDIUM;
                break;
            case 3:
                size = Size.MEDIUM;
                break;
            case 4:
                size = Size.MEDIUM;
                break;
            case 5:
                size = Size.HIGH;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + randomNumber);
        }
        return size.toString();


    }


    public static String getMarketCap() {

        Random random = new Random();
        int randomNumber = random.nextInt(3);

        // Map the random number to an enum constant
        CAP cap;
        switch (randomNumber) {
            case 0:
                cap = CAP.SMALL;
                break;
            case 1:
                cap = CAP.MID;
                break;
            case 2:
                cap = CAP.LARGE;
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + randomNumber);
        }
        return cap.toString();


    }


    public static void updateCache(String security, String stockModel, StockDataModel dataModel) throws IOException {


        //shardInfo.setPassword(REDIS_PASSWORD);
        //Jedis jedis = new Jedis(shardInfo);


        String collection = "Security";
        String collection2 = "SecurityPrice";
        String collection3 = "SecurityYield";
        jedis.hset(collection, "security", security);
        //jedis.hset(collection, "price", dataModel.getPrice().toString());
        //jedis.hset(collection, "yield",  dataModel.getAnnualyeild().toString());
        //jedis.hset(collection, "risk",getRisk());
        // jedis.hset(collection, "sector", "null");
        System.out.println(stockModel.toString());
        jedis.hset(collection, security, stockModel);
        jedis.hset(collection2, security, dataModel.getPrice().toString());
        jedis.hset(collection3, security, dataModel.getAnnualyeild().toString());


        // Retrieve the value of the key from Redis
        //  String retrievedValue = jedis.get(key);
        // System.out.println("Retrieved value: " + retrievedValue);

    }


    public static void getSecurityModel(String sec, String swing) throws IOException {


        String collectionName = "Security";
        // Set up the connection to the Azure Cosmos DB account
        //uri = new MongoClientURI(connectionString);
        //mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        MongoCollection<Document> collection = database.getCollection(collectionName);
        StockDataModel sdModel = new StockDataModel();

        Document query = new Document("_id", sec);
        MongoCursor<Document> cursor = collection.find(query).iterator();
        // Size risk = getRisk();
        int ctr = 0;
        while (cursor.hasNext()) {
            if (ctr > 0)
                break;
            Document document = cursor.next();

            try {
                sdModel = fromString(document.get(sec).toString());
                updateMongo(sec, sdModel.toString(), sdModel);
                updateCache(sec, sdModel.toString(), sdModel);


            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ctr++;
            // System.out.println();
        }


    }

    public static BigDecimal getPriceVolatality(BigDecimal oldprice) {
        BigDecimal percentage = null;
        if (swing.equals("BULL")) {

            if (getRisk().toString().equals("LOW")) {
                percentage = new BigDecimal("107");
                BigDecimal price = oldprice.multiply(percentage).divide(new BigDecimal("100"));


                //System.out.println(swing+":"+oldprice+"-"+price);
                return price;

            } else if (getRisk().toString().equals("MEDIUM")) {
                percentage = new BigDecimal("112");

                BigDecimal price = oldprice.multiply(percentage).divide(new BigDecimal("100"));
                //System.out.println(swing+":"+oldprice+"-"+price);
                return price;

                // System.out.println("BULL-"+stockModel.getQuote(true).getPrice()+"-"+price);

            } else {


                percentage = new BigDecimal("118");
                BigDecimal price = oldprice.multiply(percentage).divide(new BigDecimal("100"));
                //System.out.println(swing+":"+oldprice+"-"+price);
                return price;
                //System.out.println("BULL-"+stockModel.getQuote(true).getPrice()+"-"+price);

            }

        } else if (swing.equals("BEAR")) {


            if (getRisk().toString().equals("LOW")) {
                percentage = new BigDecimal("93");
                BigDecimal price = oldprice.multiply(percentage).divide(new BigDecimal("100"));
                //System.out.println(swing+":"+oldprice+"-"+price);
                return price;

                //System.out.println("BULL-"+stockModel.getQuote(true).getPrice()+"-"+price);

            } else if (getRisk().toString().equals("MEDIUM")) {
                percentage = new BigDecimal("88");
                BigDecimal price = oldprice.multiply(percentage).divide(new BigDecimal("100"));
                //System.out.println(swing+":"+oldprice+"-"+price);
                return price;

                // System.out.println("BULL-"+stockModel.getQuote(true).getPrice()+"-"+price);


            } else {
                percentage = new BigDecimal("82");
                BigDecimal price = oldprice.multiply(percentage).divide(new BigDecimal("100"));
                //System.out.println(swing+":"+oldprice+"-"+price);
                return price;
                //System.out.println("BULL-"+stockModel.getQuote(true).getPrice()+"-"+price);


            }


        } else {


            if (getRisk().toString().equals("LOW")) {
                percentage = new BigDecimal("101");
                BigDecimal price = oldprice.multiply(percentage).divide(new BigDecimal("100"));
                //System.out.println(swing+":"+oldprice+"-"+price);
                return price;

                //System.out.println("BULL-"+stockModel.getQuote(true).getPrice()+"-"+price);

            } else if (getRisk().toString().equals("MEDIUM")) {
                percentage = new BigDecimal("102");
                BigDecimal price = oldprice.multiply(percentage).divide(new BigDecimal("100"));
                //System.out.println(swing+":"+oldprice+"-"+price);
                return price;

                // System.out.println("BULL-"+stockModel.getQuote(true).getPrice()+"-"+price);


            } else {
                percentage = new BigDecimal("103");
                BigDecimal price = oldprice.multiply(percentage).divide(new BigDecimal("100"));
                //System.out.println(swing+":"+oldprice+"-"+price);
                return price;
                //System.out.println("BULL-"+stockModel.getQuote(true).getPrice()+"-"+price);


            }


        }


    }

    public static StockDataModel fromString(String str) throws ParseException {
        //System.out.println(str);
        StockDataModel model = new StockDataModel();
        String[] pairs = str.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

        for (String pair : pairs) {
            // System.out.println(pair);
            String[] keyValue = pair.split("=");
            String key = keyValue[0].trim();
            String value;
            try {
                value = keyValue[1].trim();
            } catch (ArrayIndexOutOfBoundsException e) {
                // TODO Auto-generated catch block
                continue;
            }
            switch (key) {
                case "date":
                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy"); // Create a date format object
                    Date date;

                    date = dateFormat.parse(value);

                    // Parse the string to a Date object
                    Calendar calendar = Calendar.getInstance(); // Get a Calendar instance
                    calendar.setTime(date); // Set the Calendar's time to the parsed date
                    model.setDate(calendar);
                    break;
                case "price":
                    BigDecimal roundedNumber = getPriceVolatality(new BigDecimal(value));
                    model.setPrice(roundedNumber.setScale(4, BigDecimal.ROUND_HALF_UP));
                    break;
                case "bid":
                    model.setBid(new BigDecimal(value));
                    break;

                case "ask":
                    model.setAsk(new BigDecimal(value));
                    break;
                case "low":
                    model.setLow(new BigDecimal(value));
                case "high":
                    model.setHigh(new BigDecimal(value));
                    break;
                case "open":
                    model.setOpen(new BigDecimal(value));
                    break;

                case "annualyeild":
                    model.setAnnualyeild(new BigDecimal(value));
                    break;
                case "symbol":
                    model.setSymbol(value);
                    break;
                case "exchange":
                    model.setExchange(value);
                    break;
                case "descitpion":
                    model.setDescitpion(value);
                    break;

                case "currency":
                    model.setCurrency(value);
                    break;
                case "industry":
                    model.setIndustry(value);
                    break;

                case "risk":
                    model.setRisk(value);
                    break;

                case "marketCap":
                    model.setMarketCap(value);
                    break;

                default:
                    break;
                // Handle unrecognized keys
            }
        }

        //System.out.println(model.toString());
        return model;
    }


    public static void main(String[] args) throws InterruptedException {
        // TODO Auto-generated method stub


        // Redis
        databaseName = "portfolio";
        swing = "MARKET";
        //JedisShardInfo shardInfo = new JedisShardInfo(REDIS_HOSTNAME, REDIS_PORT);
        shardInfo = new JedisShardInfo(REDIS_HOSTNAME, REDIS_PORT);
        shardInfo.setPassword(REDIS_PASSWORD);
        jedis = new Jedis(shardInfo);


        // Mongo
        String collectionName = "Security";
        // Set up the connection to the Azure Cosmos DB account
        //uri = new MongoClientURI(connectionString);
        //mongoClient = new MongoClient(uri);
        database = mongoClient.getDatabase(databaseName);
        collection = database.getCollection(collectionName);


        Stock stock;
        Interval interval;
        Stock s1;
        String sec;

        String[] snp500Symbols = new String[]{"A", "AAL", "AAP", "AAPL", "ABBV", "ABC", "ABMD", "ABT", "ACN", "ADBE", "ADI", "ADM", "ADP", "ADSK", "AEE", "AEP", "AES", "AFL", "AIG", "AIZ", "AJG", "AKAM", "ALB", "ALGN", "ALK", "ALL", "ALLE", "AMAT", "AMCR", "AMD", "AME", "AMGN", "AMP", "AMT", "AMZN", "ANET", "ANSS", "AON", "AOS", "APA", "APD", "APH", "APTV", "ARE", "ATO", "ATVI", "AVB", "AVGO", "AVY", "AWK", "AXP", "AZO", "BA", "BAC", "BAX", "BBY", "BDX", "BEN", "BF-B", "BIIB", "BIO", "BK", "BKNG", "BKR", "BLK", "BMY", "BR", "BRK-B", "BSX", "BWA", "BXP", "C", "CAG", "CAH", "CARR", "CAT", "CB", "CBOE", "CBRE", "CCI", "CCL", "CDNS", "CDW", "CE", "CF", "CFG", "CHD", "CHRW", "CHTR", "CI", "CINF", "CL", "CLX", "CMA", "CMCSA", "CME", "CMG", "CMI", "CMS", "CNC", "CNP", "COF", "COO", "COP", "COST", "CPB", "CPRT", "CRM", "CSCO", "CSX", "CTAS", "CTLT", "CTSH", "CTVA", "CVS", "CVX", "CZR", "D", "DAL", "DD", "DE", "DFS", "DG", "DGX", "DHI", "DHR", "DIS", "DISH", "DLR", "DLTR", "DOV", "DOW", "DPZ", "DRI", "DTE", "DUK", "DVA", "DVN", "DXC", "DXCM", "EA", "EBAY", "ECL", "ED", "EFX", "EIX", "EL", "EMN", "EMR", "ENPH", "EOG", "EQIX", "EQR", "ES", "ESS", "ETN", "ETR", "ETSY", "EVRG", "EW", "EXC", "EXPD", "EXPE", "EXR", "F", "FANG", "FAST", "FCX", "FDX", "FE", "FFIV", "FIS", "FISV", "FITB", "FLT", "FMC", "FOX", "FOXA", "FRC", "FRT", "FTNT", "FTV", "GD", "GE", "GILD", "GIS", "GL", "GLW", "GM", "GNRC", "GOOG", "GOOGL", "GPC", "GPN", "GPS", "GRMN", "GS", "GWW", "HAL", "HAS", "HBAN", "HBI", "HCA", "HD", "HES", "HIG", "HII", "HLT", "HOLX", "HON", "HPE", "HPQ", "HRL", "HSIC", "HST", "HSY", "HUM", "HWM", "IBM", "ICE", "IDXX", "IEX", "IFF", "ILMN", "INCY", "INTC", "INTU", "IP", "IPG", "IPGP", "IQV", "IR", "IRM", "ISRG", "IT", "ITW", "IVZ", "J", "JBHT", "JCI", "JKHY", "JNJ", "JNPR", "JPM", "K", "KEY", "KEYS", "KHC", "KIM", "KLAC", "KMB", "KMI", "KMX", "KO", "KR", "L", "LDOS", "LEG", "LEN", "LH", "LHX", "LIN", "LKQ", "LLY", "LMT", "LNC", "LNT", "LOW", "LRCX", "LUMN", "LUV", "LVS", "LW", "LYB", "LYV", "MA", "MAA", "MAR", "MAS", "MCD", "MCHP", "MCK", "MCO", "MDLZ", "MDT", "MET", "MGM", "MHK", "MKC", "MKTX", "MLM", "MMC", "MMM", "MNST", "MO", "MOS", "MPC", "MPWR", "MRK", "MRO", "MS", "MSCI", "MSFT", "MSI", "MTB", "MTD", "MU", "NCLH", "NDAQ", "NEE", "NEM", "NFLX", "NI", "NKE", "NOC", "NOV", "NOW", "NRG", "NSC", "NTAP", "NTRS", "NUE", "NVDA", "NVR", "NWL", "NWS", "NWSA", "NXPI", "O", "ODFL", "OKE", "OMC", "ORCL", "ORLY", "OTIS", "OXY", "PAYC", "PAYX", "PCAR", "PEAK", "PEG", "PENN", "PEP", "PFE", "PFG", "PG", "PGR", "PH", "PHM", "PKG", "PKI", "PLD", "PM", "PNC", "PNR", "PNW", "POOL", "PPG", "PPL", "PRGO", "PRU", "PSA", "PSX", "PTC", "PVH", "PWR", "PXD", "PYPL", "QCOM", "QRVO", "RCL", "RE", "REG", "REGN", "RF", "RHI", "RJF", "RL", "RMD", "ROK", "ROL", "ROP", "ROST", "RSG", "RTX", "SBAC", "SBUX", "SCHW", "SEE", "SHW", "SIVB", "SJM", "SLB", "SNA", "SNPS", "SO", "SPG", "SPGI", "SRE", "STE", "STT", "STX", "STZ", "SWK", "SWKS", "SYF", "SYK", "SYY", "T", "TAP", "TDG", "TDY", "TEL", "TER", "TFC", "TFX", "TGT", "TJX", "TMO", "TMUS", "TPR", "TRMB", "TROW", "TRV", "TSCO", "TSLA", "TSN", "TT", "TTWO", "TXN", "TXT", "TYL", "UA", "UAA", "UAL", "UDR", "UHS", "ULTA", "UNH", "UNM", "UNP", "UPS", "URI", "USB", "V", "VFC", "VLO", "VMC", "VNO", "VRSK", "VRSN", "VRTX", "VTR", "VTRS", "VZ", "WAB", "WAT", "WBA", "WDC", "WEC", "WELL", "WFC", "WHR", "WM", "WMB", "WMT", "WRB", "WRK", "WST", "WU", "WY", "WYNN", "XEL", "XOM", "XRAY", "XYL", "YUM", "ZBH", "ZBRA", "ZION", "ZTS"};

        //String[] snp500Symbols = new String[] {"A","AAL"};
		
		
	/*	try {
			
		
			System.out.println("*************Hey Welcome to Stock!!**************************");
		
			System.out.println("Swing:"+swing);*/

        //Map<String, Stock> symbols = YahooFinance.get(snp500Symbols);
        int ctr = 0;
        while (true) {
            //System.out.println("Iteration Number"+ctr);
            for (int i = 0; i < snp500Symbols.length; i++) {
                sec = snp500Symbols[i];
                try {
                    getSecurityModel(sec, swing);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    mongoClient.close();
                    jedis.close();
                }
                // System.out.println(stock.getName());
                //System.out.println(stock.getQuote().getSymbol());

            }
            ctr++;
        }
		/*} catch (IOException e) {
			
			System.out.println("Somethign went wrong..........");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/


    }

}
