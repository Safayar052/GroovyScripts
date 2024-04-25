// Import necessary classes
import java.nio.file.*
import java.nio.charset.*
import java.util.Properties
import java.net.URLDecoder

// Load the properties file
def properties = new Properties()
properties.load(new File('config.properties').newReader())

// ISH pipelien search token
def searchTokenPipeline = properties.getProperty('searchTokenPipeline')

// REST call search token
def searchTokenRest = properties.getProperty('searchTokenRest')


// Regex pattern to get search term
def pattern = /earchTerm=([^&|]+)/

// Set to store unique Search Terms
Set<String> uniqueSearchKeys = new HashSet<>()

if (args.length > 0) {
    // Get directory path
    def directoryPath = args[0]

    // Output file path
    def outputPath = "searchTerms.csv"
    if(args.length > 1) {
        outputPath = args[1]
    }
    
    //sample code for read file with log extenstion

    // Define the directory path
    def directory = new File(directoryPath)

    // Filter files with .log extension and a certain prefix
    def logFiles = directory.listFiles { file ->
        file.isFile() && file.name.startsWith("WA-") && file.name.endsWith(".log")
    }

    // Process the filtered files
    logFiles.each { file ->
        file.eachLine { line ->
            if (line.contains(searchTokenPipeline) || line.contains(searchTokenRest)) {
                def matcher = (line =~ pattern)

                if (matcher.find()) {
                    def searchTerm = matcher.group(1)
                
                    // Find the index of the first space character
                    def indexOfSpace = searchTerm.indexOf(' ')

                    // If a space is found, trim all characters after it
                    def formattedSearchTerm = ((indexOfSpace >= 0 ? searchTerm.substring(0, indexOfSpace) : searchTerm).replaceAll("\\+", " ")).trim()
                    
                    if (formattedSearchTerm.length() > 2) {
                        try {
                            // URL decode the string
                            def decodedString = URLDecoder.decode(formattedSearchTerm, "UTF-8")
                            uniqueSearchKeys.add(decodedString)
                        } catch (UnsupportedEncodingException e) {
                            println "Error decoding the URL-encoded string: ${e.message}"
                            uniqueSearchKeys.add(formattedSearchTerm)
                        }                       
                    }
                }
            }
        }
    }

    //println uniqueSearchKeys

    //println uniqueSearchKeys.size()

    try {
        // Create a FileWriter instance with the file path
        def writer = new PrintWriter(new BufferedWriter(new FileWriter(outputPath)))

        // Write the content to the file
        uniqueSearchKeys.each { searchTerm ->
            writer.println(searchTerm)
        }
        
        // Close the writer
        writer.close()

        println "Content has been written to $outputPath"
    } catch (IOException e) {
        println "An error occurred: ${e.message}"
    }

} else {
    println "No directory path is provided"
}