package huex.training;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Netflix {
	public static Set<String> type = new HashSet<>();
	public static Set<String> listedin = new HashSet<>();
	public static Set<String> countries = new HashSet<>();
	public static List<NetflixData> parsedData = new ArrayList<>();
	public static int headerLength;

	/**
	 * This method will update our type and listedin fields so that we know what our
	 * inputs will be Like
	 * 
	 * @param parsedData
	 */
	public static List<String> cacheKnowledgeAndGetListin(String type, String listedIn) {
		Netflix.type.add(type.trim());
		if (listedIn != null && !listedIn.isEmpty()) {
			Arrays.stream(listedIn.replaceAll("\"", "").trim().split(","))
					.forEach(listings -> Netflix.listedin.add(listings.trim()));
			;
			return Arrays.stream(listedIn.replaceAll("\"", "").trim().split(",")).collect(Collectors.toList());
		}
		return new ArrayList<String>();
	}

	/**
	 * This method will check if the format is valid or not
	 * 
	 * @param format
	 * @param value
	 * @return
	 */
	public static boolean isValidFormat(String format, String value) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			date = sdf.parse(value);
			if (!value.equals(sdf.format(date))) {
				date = null;
			}
		} catch (ParseException ex) {
			return false;
		}
		return true;
	}

	/**
	 * This method will remove the spaces and quotes from the string
	 * 
	 * @param dateNetflixData
	 * @return
	 */
	public static String getCleanDate(String date) {
		if (date != null) {
			date.replaceAll("\"", "");
			date.replaceAll(",", "");
			date.trim();
			date.replaceAll(" ", "-");
		}
		return date;
	}

	/**
	 * This will get the date in epoch time based on two format given in dataset.
	 * One could have also form a format array and iterate it to try different
	 * formats in case of any format is given
	 * 
	 * @param dateNetflixData
	 * @return
	 */
	public static long getDateFromNetflixData(String dateNetflixData) {
		long date = 0;
		String format = "";
		String cleanDate = getCleanDate(dateNetflixData);
		if (cleanDate == null)
			return 0;
		if (isValidFormat("dd-MMM-yy", cleanDate)) {
			format = "dd-MMM-yy";
		} else if (isValidFormat("MMMM-dd-yyyy", cleanDate)) {
			format = "MMMM-dd-yyyy";
		}
		try {
			if (!format.isEmpty()) {
				SimpleDateFormat dateFormatter = new SimpleDateFormat(format);
				date = dateFormatter.parse(cleanDate).getTime() / 1000;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * This method will get the list of countries
	 * 
	 * @param countriesNetflixData
	 * @return
	 */
	public static List<String> getCountryList(String countriesNetflixData) {
		List<String> countries = new ArrayList<>();
		if (countriesNetflixData != null)
			Arrays.stream(countriesNetflixData.replaceAll("\"", "").split(","))
					.forEach(country -> countries.add(country.trim()));
		;
		countries.forEach(country -> Netflix.countries.add(country));
		return countries;
	}

	/**
	 * This method will parse the data of xlsx/csv file and PRE-compute the result
	 * for fast query time I've not used path and regex(parameters) values inside
	 * method so that reviewer can easily get the file path and change it for
	 * testing else i could have put the file path in Application Constants and used
	 * that accordingly.
	 * 
	 * @param path
	 * @param regex
	 * @return
	 */

	public static List<NetflixData> parseData(String path, String regex) {
		List<NetflixData> parsedData = new ArrayList<>();
		String file = "/Users/akhilnigam/Documents/netflix_titles.csv";// \"([^\"]*)\"
		String line;
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			line = br.readLine();
			// First line/row should will be the header
			headerLength = line.split(",").length;
			while ((line = br.readLine()) != null) {
				Pattern p = Pattern.compile("(?:^|,)(\\\"(?:[^\\\"]+|\\\"\\\")*\\\"|[^,]*)");
				Matcher m = p.matcher(line);
				Object[] headers = new Object[headerLength + 1];
				int count = 0;
				while (m.find()) {
					headers[count++] = (String) (m.group(1).trim());
				}
				if (headers[0] != null && !headers[0].toString().isEmpty()
						&& headers[0].toString().matches(".*\\d.*")) {
					headers[5] = (List<String>) getCountryList((String) headers[5]);
					headers[6] = (long) getDateFromNetflixData((String) headers[6]);
					headers[10] = (List<String>) cacheKnowledgeAndGetListin((String) headers[1], (String) headers[10]);
					NetflixData netflixData = new NetflixData(headers);
					parsedData.add(netflixData);
				}
			}
			return parsedData;
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();

		}
		return parsedData;
	}

	public static boolean isDate(String inp) {
		if (isValidFormat("dd-MMM-yy", inp)) {
			return true;
		} else if (isValidFormat("MMMM-dd-yyyy", inp)) {
			return true;
		} else
			return false;
	}

	public static List<NetflixData> getQueryResult(int n, List<NetflixData> parsedData, String[] args) {
		String query = String.join(" ", args);
		List<NetflixData> result = new ArrayList<>();
		query.trim();
		// Checking if query contains Listing first
		boolean isListed = false, isType = false;
		String listedQuery = "";
		String typeQuery = "";
		for (String listed : listedin) {
			if (query.contains(listed)) {
				isListed = true;
				if (listedQuery.length() < listed.length())
					listedQuery = listed;
			}
		}
		String buildInput = "";
		if (isListed) {
			buildInput = Integer.toString(n) + " " + listedQuery;
			if (buildInput.equals(query)) {
				result = Filter.filterByListedIn(parsedData, n, listedQuery.trim());
			} else {
				String[] dates = query.replaceAll(buildInput, "").trim().split(" ");
				long startDate = getDateFromNetflixData(dates[0]);
				long endDate = getDateFromNetflixData(dates[1]);
				result = Filter.filterByListedInAndDate(parsedData, n, listedQuery, startDate, endDate);
			}
		} else {
			// checking if type exists
			for (String typeIn : type) {
				if (query.contains(typeIn)) {
					isType = true;
					// System.out.println("TYPE TRUE");
					typeQuery = typeIn;
					break;
				}
			}
			if (isType) {
				// checking if country is present
				boolean isCountry = false;
				String countryQuery = "";
				for (String country : countries) {
					if (country != null && !country.isEmpty()) {
						if (query.trim().contains(country)) {
							isCountry = true;
							countryQuery = country;
							break;
						}

					}
				}
				buildInput = Integer.toString(n) + " " + typeQuery + " " + countryQuery;
				// checking if date option is given
				if (!buildInput.trim().equals(query.trim())) {
					String[] dates = query.replaceAll(buildInput, "").trim().split(" ");
					try {
						long startDate = getDateFromNetflixData(dates[0]);
						long endDate = getDateFromNetflixData(dates[1]);
						if (startDate == 0 || endDate == 0)
							throw new Exception("Illegal Argument");
						if (isCountry) {
							result = Filter.filterByTypeCountryAndDate(parsedData, n, typeQuery, countryQuery,
									startDate, endDate);
						} else
							result = Filter.filterByTypeAndDate(parsedData, n, typeQuery, startDate, endDate);
					} catch (Exception e) {
						System.out.print(ApplicationConstants.ILLEGAL_ARGUMENTS);
					}

				} else {
					if (isCountry) {
						result = Filter.filterByTypeAndCountry(parsedData, n, typeQuery, countryQuery);
					} else
						result = Filter.filterByType(parsedData, n, typeQuery);
				}
			}

		}
		return result;
	}

	// Since static block will run first before main that will help in pre-computing
	// values
	// for faster query time
	static {
		parsedData = parseData("", "");
	}

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		List<NetflixData> result = new ArrayList<>();
		int n = 0;
		try {
			n = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			System.out.print(ApplicationConstants.NOT_A_NUMBER);
		}

		result = getQueryResult(n, parsedData, args);
		if (result.size() != 0)
			result.forEach(res -> System.out.println(res));
		else
			System.out.println(ApplicationConstants.NO_RECORDS_FOUND);
		long endTime = System.currentTimeMillis();
		System.out.println(
				ApplicationConstants.QUERY_TIME_IN_MILLISECONDS + " " + (endTime - startTime) + " miliseconds");
	}
}
