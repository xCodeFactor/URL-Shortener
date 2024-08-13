package com.example.urlshortener.controller;

import com.example.urlshortener.service.UrlShortenerService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;

@Controller
@RestController
@RequestMapping("/api/v1/urls")
public class UrlShortenerController {

    @Autowired
    private UrlShortenerService urlShortenerService;

    @GetMapping("/")
    public String landingPage() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>URL Shortener - Home</title>\n" +
                "    <!-- Tailwind CSS -->\n" +
                "    <link href=\"https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css\" rel=\"stylesheet\">\n" +
                "    <style>\n" +
                "        body {\n" +
                "            background: url('/images/background.jpg') no-repeat center fixed;\n" +
                "            background-size: cover;\n" +
                "            animation: fadeInBackground 2s ease-in-out forwards;\n" +
                "        }\n" +
                "\n" +
                "        @keyframes fadeInBackground {\n" +
                "            from {\n" +
                "                opacity: 0;\n" +
                "            }\n" +
                "            to {\n" +
                "                opacity: 1;\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        .container {\n" +
                "            opacity: 0;\n" +
                "            animation: fadeInContainer 1.5s ease-in-out forwards 0.5s;\n" +
                "            background-color: transparent; /* Make container fully transparent */\n" +
                "        }\n" +
                "\n" +
                "        @keyframes fadeInContainer {\n" +
                "            from {\n" +
                "                opacity: 0;\n" +
                "                transform: translateY(20px);\n" +
                "            }\n" +
                "            to {\n" +
                "                opacity: 1;\n" +
                "                transform: translateY(0);\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        .button {\n" +
                "            transition: background-color 0.3s ease, color 0.3s ease, transform 0.3s ease;\n" +
                "        }\n" +
                "\n" +
                "        .button:hover {\n" +
                "            background-color: transparent;\n" +
                "            color: #1D4ED8; /* dark blue */\n" +
                "            transform: translateY(-5px); /* Pop up effect on hover */\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body class=\"flex items-center justify-center min-h-screen\">\n" +
                "<div class=\"container mx-auto p-6 bg-blue-800 bg-opacity-70 shadow-lg rounded-lg w-full max-w-md\">\n" +
                "    <h1 class=\"text-2xl font-bold mb-6 text-white text-center\">URL Shortener</h1>\n" +
                "    <div class=\"space-y-4\">\n" +
                "        <a href=\"/api/v1/urls/shorten\" class=\"button block w-full px-4 py-2 bg-white text-blue-500 font-semibold rounded-lg shadow-md text-center\">Shorten URL</a>\n" +
                "        <a href=\"/api/v1/urls/redirect\" class=\"button block w-full px-4 py-2 bg-white text-blue-500 font-semibold rounded-lg shadow-md text-center\">Access Original Link</a>\n" +
                "        <a href=\"/api/v1/urls/update\" class=\"button block w-full px-4 py-2 bg-white text-blue-500 font-semibold rounded-lg shadow-md text-center\">Update Shortened URL</a>\n" +
                "        <a href=\"/api/v1/urls/update-expiry\" class=\"button block w-full px-4 py-2 bg-white text-blue-500 font-semibold rounded-lg shadow-md text-center\">Update URL Expiry</a>\n" +
                "    </div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>\n";
    }

    @GetMapping("/shorten")
    public String shortPage() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Shorten URL</title>\n" +
                "    <link href=\"https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css\" rel=\"stylesheet\">\n" +
                "    <style>\n" +
                "        body {\n" +
                "            background: url('/images/background.jpg') no-repeat center fixed;\n" +
                "            background-size: cover;\n" +
                "        }\n" +
                "        #shortUrlContainer { display: none; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body class=\"flex items-center justify-center min-h-screen\">\n" +
                "<div class=\"container mx-auto p-6 bg-blue-500 bg-opacity-70 shadow-lg rounded-lg w-full max-w-md\">\n" +
                "    <h1 class=\"text-2xl font-bold mb-6 text-white text-center\">Shorten URL</h1>\n" +
                "    <form id=\"shortenForm\" class=\"space-y-4\">\n" +
                "        <div class=\"flex flex-col\">\n" +
                "            <label for=\"longUrl\" class=\"text-sm font-medium text-white\">Original URL</label>\n" +
                "            <input type=\"url\" id=\"longUrl\" name=\"longUrl\" placeholder=\"Enter the URL to shorten\" required\n" +
                "                   class=\"mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm\">\n" +
                "        </div>\n" +
                "        <button type=\"submit\" class=\"w-full px-4 py-2 bg-white text-blue-500 font-semibold rounded-lg shadow-md hover:bg-opacity-0 hover:text-blue-700 transition duration-300 ease-in-out focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500\">\n" +
                "            Shorten\n" +
                "        </button>\n" +
                "    </form>\n" +
                "    <div id=\"shortUrlContainer\" class=\"mt-6 p-4 bg-teal-100 border border-teal-300 rounded-md\">\n" +
                "        <p class=\"text-teal-600\">Shortened URL: <a id=\"shortUrl\" href=\"#\" target=\"_blank\" class=\"font-semibold underline\"></a></p>\n" +
                "    </div>\n" +
                "</div>\n" +
                "\n" +
                "<script>\n" +
                "    document.getElementById('shortenForm').addEventListener('submit', async function (event) {\n" +
                "        event.preventDefault();\n" +
                "\n" +
                "        const longUrl = document.getElementById('longUrl').value;\n" +
                "        const responseContainer = document.getElementById('shortUrlContainer');\n" +
                "        const shortUrlElement = document.getElementById('shortUrl');\n" +
                "\n" +
                "        try {\n" +
                "            const response = await fetch('/api/v1/urls/shorten', {\n" +
                "                method: 'POST',\n" +
                "                headers: {\n" +
                "                    'Content-Type': 'application/json'\n" +
                "                },\n" +
                "                body: JSON.stringify(longUrl)\n" +
                "            });\n" +
                "\n" +
                "            if (!response.ok) throw new Error('Failed to shorten URL');\n" +
                "            const data = await response.text();\n" +
                "            shortUrlElement.textContent = data;\n" +
                "            shortUrlElement.href = data;\n" +
                "            responseContainer.style.display = 'block';\n" +
                "        } catch (error) {\n" +
                "            console.error('Error:', error);\n" +
                "            alert('Error shortening URL. Please try again later.');\n" +
                "        }\n" +
                "    });\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>\n";
    }

    @PostMapping("/shorten")
    public ResponseEntity<String> shortenUrl(@RequestBody String originalUrl) {
        // Remove any quotes if they exist
        if (originalUrl.startsWith("\"") && originalUrl.endsWith("\"")) {
            originalUrl = originalUrl.substring(1, originalUrl.length() - 1);
        }

        if (originalUrl.isBlank()) {
            return ResponseEntity.badRequest().body("Invalid URL");
        }

        String shortUrl = urlShortenerService.shortenUrl(originalUrl.trim());
        return ResponseEntity.ok(shortUrl);
    }


    @GetMapping("/update")
    public String updatePage(){
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Update URL</title>\n" +
                "    <!-- Tailwind CSS -->\n" +
                "    <link href=\"https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css\" rel=\"stylesheet\">\n" +
                "    <style>\n" +
                "        /* Additional custom styles */\n" +
                "        body {\n" +
                "            background: url('/images/background.jpg') no-repeat center fixed;\n" +
                "            background-size: cover;\n" +
                "        }\n" +
                "        #updateResult { display: none; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body class=\"flex items-center justify-center min-h-screen\">\n" +
                "<div class=\"container mx-auto p-6 bg-blue-500 bg-opacity-70 shadow-lg rounded-lg w-full max-w-md\">\n" +
                "    <h1 class=\"text-2xl font-bold mb-6 text-white text-center\">Update URL</h1>\n" +
                "    <form id=\"updateForm\" class=\"space-y-4\">\n" +
                "        <div class=\"flex flex-col\">\n" +
                "            <label for=\"shortUrl\" class=\"text-sm font-medium text-white\">Short Code</label>\n" +
                "            <input type=\"text\" id=\"shortUrl\" placeholder=\"Enter short code\" required\n" +
                "                   class=\"mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm\">\n" +
                "        </div>\n" +
                "        <div class=\"flex flex-col\">\n" +
                "            <label for=\"newDestinationUrl\" class=\"text-sm font-medium text-white\">New Destination URL</label>\n" +
                "            <input type=\"url\" id=\"newDestinationUrl\" placeholder=\"Enter the new destination URL\" required\n" +
                "                   class=\"mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm\">\n" +
                "        </div>\n" +
                "        <button type=\"submit\" class=\"w-full px-4 py-2 bg-white text-blue-500 font-semibold rounded-lg shadow-md hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500\">\n" +
                "            Update\n" +
                "        </button>\n" +
                "    </form>\n" +
                "    <div id=\"updateResult\" class=\"mt-6 p-4 bg-teal-100 border border-teal-300 rounded-md\">\n" +
                "        <p class=\"text-teal-600\">Update successful</p>\n" +
                "    </div>\n" +
                "</div>\n" +
                "\n" +
                "<script>\n" +
                "    document.getElementById('updateForm').addEventListener('submit', async function (event) {\n" +
                "        event.preventDefault();\n" +
                "        const shortUrl = document.getElementById('shortUrl').value;\n" +
                "        const newDestinationUrl = document.getElementById('newDestinationUrl').value;\n" +
                "        const resultContainer = document.getElementById('updateResult');\n" +
                "\n" +
                "        try {\n" +
                "            const response = await fetch('/api/v1/urls/update?shortUrl=' + encodeURIComponent(shortUrl) + '&newDestinationUrl=' + encodeURIComponent(newDestinationUrl), {\n" +
                "                method: 'POST'\n" +
                "            });\n" +
                "\n" +
                "            if (!response.ok) throw new Error('Failed to update URL');\n" +
                "            const success = await response.json();\n" +
                "            if (success) {\n" +
                "                resultContainer.style.display = 'block';\n" +
                "            } else {\n" +
                "                alert('Update failed. Please try again.');\n" +
                "            }\n" +
                "        } catch (error) {\n" +
                "            console.error('Error:', error);\n" +
                "            alert('Error updating URL. Please try again later.');\n" +
                "        }\n" +
                "    });\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>\n";
    }

    @PostMapping("/update")
    public ResponseEntity<Boolean> updateUrl(@RequestParam String shortUrl, @RequestParam String newDestinationUrl) {
        // Trim and sanitize inputs
        shortUrl = shortUrl.trim();
        newDestinationUrl = newDestinationUrl.trim();

        if (shortUrl.isEmpty() || newDestinationUrl.isEmpty()) {
            return ResponseEntity.badRequest().body(false);
        }

        boolean updated = urlShortenerService.updateUrl(shortUrl, newDestinationUrl);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/redirect")
    public String redirectPage(){
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>URL Redirect</title>\n" +
                "    <!-- Tailwind CSS -->\n" +
                "    <link href=\"https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css\" rel=\"stylesheet\">\n" +
                "    <style>\n" +
                "        /* Additional custom styles */\n" +
                "        body {\n" +
                "            background: url('/images/background.jpg') no-repeat center fixed;\n" +
                "            background-size: cover;\n" +
                "        }\n" +
                "        #redirectResult { display: none; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body class=\"flex items-center justify-center min-h-screen\">\n" +
                "<div class=\"container mx-auto p-6 bg-blue-500 bg-opacity-70 shadow-lg rounded-lg w-full max-w-md\">\n" +
                "    <h1 class=\"text-2xl font-bold mb-6 text-white text-center\">URL Redirect</h1>\n" +
                "    <form id=\"redirectForm\" class=\"space-y-4\">\n" +
                "        <div class=\"flex flex-col\">\n" +
                "            <label for=\"shortUrl\" class=\"text-sm font-medium text-white\">Code</label>\n" +
                "            <input type=\"text\" id=\"shortUrl\" placeholder=\"Enter short code\" required\n" +
                "                   class=\"mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm\">\n" +
                "        </div>\n" +
                "        <button type=\"submit\" class=\"w-full px-4 py-2 bg-white text-blue-500 font-semibold rounded-lg shadow-md hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500\">\n" +
                "            Go\n" +
                "        </button>\n" +
                "    </form>\n" +
                "    <div id=\"redirectResult\" class=\"mt-6 p-4 bg-teal-100 border border-teal-300 rounded-md\">\n" +
                "        <p class=\"text-teal-600\">Redirecting to: <a id=\"redirectUrl\" href=\"#\" target=\"_blank\" class=\"font-semibold underline\"></a></p>\n" +
                "    </div>\n" +
                "</div>\n" +
                "\n" +
                "<script>\n" +
                "    document.getElementById('redirectForm').addEventListener('submit', async function (event) {\n" +
                "        event.preventDefault();\n" +
                "        const shortUrl = document.getElementById('shortUrl').value;\n" +
                "        const resultContainer = document.getElementById('redirectResult');\n" +
                "        const redirectUrlElement = document.getElementById('redirectUrl');\n" +
                "\n" +
                "        try {\n" +
                "            const response = await fetch(`/api/v1/urls/${shortUrl}`);\n" +
                "            if (!response.ok) throw new Error('Failed to redirect');\n" +
                "            const fullUrl = await response.text();\n" +
                "            redirectUrlElement.textContent = fullUrl;\n" +
                "            redirectUrlElement.href = fullUrl;\n" +
                "            resultContainer.style.display = 'block';\n" +
                "        } catch (error) {\n" +
                "            console.error('Error:', error);\n" +
                "            alert('Error retrieving URL. Please try again later.');\n" +
                "        }\n" +
                "    });\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>\n";
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<String> redirectToFullUrl(@PathVariable String shortCode) {
        String fullUrl = urlShortenerService.getOriginalUrl(shortCode);
        if (fullUrl == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("URL not found");
        }
        return ResponseEntity.ok().body(fullUrl);
    }

    @GetMapping("/update-expiry")
    public  String updateExpiryPage(){
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Update URL Expiry</title>\n" +
                "    <!-- Tailwind CSS -->\n" +
                "    <link href=\"https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css\" rel=\"stylesheet\">\n" +
                "    <style>\n" +
                "        /* Additional custom styles */\n" +
                "        body {\n" +
                "            background: url('/images/background.jpg') no-repeat center fixed;\n" +
                "            background-size: cover;\n" +
                "        }\n" +
                "        #updateExpiryResult { display: none; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body class=\"flex items-center justify-center min-h-screen\">\n" +
                "<div class=\"container mx-auto p-6 bg-blue-500 bg-opacity-70 shadow-lg rounded-lg w-full max-w-md\">\n" +
                "    <h1 class=\"text-2xl font-bold mb-6 text-white text-center\">Update URL Expiry</h1>\n" +
                "    <form id=\"updateExpiryForm\" class=\"space-y-4\">\n" +
                "        <div class=\"flex flex-col\">\n" +
                "            <label for=\"shortUrl\" class=\"text-sm font-medium text-white\">Short Code</label>\n" +
                "            <input type=\"text\" id=\"shortUrl\" placeholder=\"Enter short code\" required\n" +
                "                   class=\"mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm\">\n" +
                "        </div>\n" +
                "        <div class=\"flex flex-col\">\n" +
                "            <label for=\"daysToAdd\" class=\"text-sm font-medium text-white\">Days to Add</label>\n" +
                "            <input type=\"number\" id=\"daysToAdd\" placeholder=\"Enter number of days\" required\n" +
                "                   class=\"mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm\">\n" +
                "        </div>\n" +
                "        <button type=\"submit\" class=\"w-full px-4 py-2 bg-white text-blue-500 font-semibold rounded-lg shadow-md hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500\">\n" +
                "            Update\n" +
                "        </button>\n" +
                "    </form>\n" +
                "    <div id=\"updateExpiryResult\" class=\"mt-6 p-4 bg-teal-100 border border-teal-300 rounded-md\">\n" +
                "        <p class=\"text-teal-600\">Expiry updated successfully</p>\n" +
                "    </div>\n" +
                "</div>\n" +
                "\n" +
                "<script>\n" +
                "    document.getElementById('updateExpiryForm').addEventListener('submit', async function (event) {\n" +
                "        event.preventDefault();\n" +
                "        const shortUrl = document.getElementById('shortUrl').value;\n" +
                "        const daysToAdd = document.getElementById('daysToAdd').value;\n" +
                "        const resultContainer = document.getElementById('updateExpiryResult');\n" +
                "\n" +
                "        try {\n" +
                "            const response = await fetch('/api/v1/urls/update-expiry?shortUrl=' + encodeURIComponent(shortUrl) + '&daysToAdd=' + encodeURIComponent(daysToAdd), {\n" +
                "                method: 'POST'\n" +
                "            });\n" +
                "\n" +
                "            if (!response.ok) throw new Error('Failed to update expiry');\n" +
                "            const success = await response.json();\n" +
                "            if (success) {\n" +
                "                resultContainer.style.display = 'block';\n" +
                "            } else {\n" +
                "                alert('Update failed. Please try again.');\n" +
                "            }\n" +
                "        } catch (error) {\n" +
                "            console.error('Error:', error);\n" +
                "            alert('Error updating expiry. Please try again later.');\n" +
                "        }\n" +
                "    });\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>\n";
    }

    @PostMapping("/update-expiry")
    public ResponseEntity<Boolean> updateExpiry(@RequestParam String shortUrl, @RequestParam int daysToAdd) {
        boolean updated = urlShortenerService.updateExpiry(shortUrl, daysToAdd);
        return ResponseEntity.ok(updated);
    }
}