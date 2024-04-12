// Import necessary classes
import java.nio.file.*
import java.nio.charset.*

// ISH pipelien search token
def searchTokenPipeline = "ViewParametricSearch"

// REST call search token
def searchTokenRest = "products?searchTerm"


// Regex pattern to get search term
def pattern = /earchTerm=([^&|]+)/

// Set to store unique Search Terms
Set<String> uniqueSearchKeys = new HashSet<>()

if (args.length > 0) {
    // Get directory path
    def directoryPath = args[0]

    // Traverse each file in the directory
    Files.walk(Paths.get(directoryPath)).each { path ->
        if (Files.isDirectory(path)) {
            // Skip directories
            return
        }

        // Read each line of the file and process it
        Files.lines(path, StandardCharsets.UTF_8).each { line ->
            if (line.contains(searchTokenPipeline) || line.contains(searchTokenRest)) {
                def matcher = (line =~ pattern)

                if (matcher.find()) {
                    def searchTerm = matcher.group(1)
                    if (searchTerm.length() > 2) {
                        // Find the index of the first space character
                        def indexOfSpace = searchTerm.indexOf(' ')

                        // If a space is found, trim all characters after it
                        def formattedSearchTerm = (indexOfSpace >= 0 ? searchTerm.substring(0, indexOfSpace) : searchTerm).replaceAll("\\+", " ")

                        uniqueSearchKeys.add(formattedSearchTerm)
                    }
                }
            }
        }
    }

    println uniqueSearchKeys

    println uniqueSearchKeys.size()
} else {
    println "No directory path is provided"
}