// URL Shortener Frontend JavaScript

document.addEventListener('DOMContentLoaded', function() {
    const urlForm = document.getElementById('urlForm');
    const resultDiv = document.getElementById('result');
    const statsDiv = document.getElementById('stats');
    const copyBtn = document.getElementById('copyBtn');
    const statsBtn = document.getElementById('statsBtn');

    // Form submission
    urlForm.addEventListener('submit', async function(e) {
        e.preventDefault();

        const originalUrl = document.getElementById('originalUrl').value;
        const customCode = document.getElementById('customCode').value;

        // Show loading state
        const submitBtn = urlForm.querySelector('button[type="submit"]');
        const originalText = submitBtn.innerHTML;
        submitBtn.innerHTML = '<span class="loading"></span> Shortening...';
        submitBtn.disabled = true;

        try {
            let response;
            if (customCode.trim()) {
                // Use custom code endpoint
                response = await fetch('/api/shorten/custom', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        url: originalUrl,
                        customCode: customCode
                    })
                });
            } else {
                // Use regular shorten endpoint
                response = await fetch('/api/shorten', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        url: originalUrl
                    })
                });
            }

            const data = await response.json();

            if (response.ok) {
                showResult(data);
                showMessage('URL shortened successfully!', 'success');
            } else {
                showMessage(data.error || 'Failed to shorten URL', 'error');
            }
        } catch (error) {
            console.error('Error:', error);
            showMessage('Network error. Please try again.', 'error');
        } finally {
            // Reset button
            submitBtn.innerHTML = originalText;
            submitBtn.disabled = false;
        }
    });

    // Copy to clipboard
    copyBtn.addEventListener('click', function() {
        const shortUrl = document.getElementById('shortUrlLink').textContent;
        navigator.clipboard.writeText(shortUrl).then(function() {
            showMessage('URL copied to clipboard!', 'success');
        }).catch(function(err) {
            console.error('Failed to copy: ', err);
            showMessage('Failed to copy URL', 'error');
        });
    });

    // Show statistics
    statsBtn.addEventListener('click', async function() {
        const shortCode = document.getElementById('shortCodeDisplay').textContent;

        try {
            const response = await fetch(`/api/stats/${shortCode}`);
            const data = await response.json();

            if (response.ok) {
                showStats(data);
            } else {
                showMessage('Failed to load statistics', 'error');
            }
        } catch (error) {
            console.error('Error:', error);
            showMessage('Network error. Please try again.', 'error');
        }
    });

    function showResult(data) {
        document.getElementById('originalUrlDisplay').textContent = data.originalUrl;
        document.getElementById('shortUrlLink').textContent = data.shortUrl;
        document.getElementById('shortUrlLink').href = data.shortUrl;
        document.getElementById('shortCodeDisplay').textContent = data.shortCode;

        resultDiv.classList.remove('hidden');
        statsDiv.classList.add('hidden');

        // Scroll to result
        resultDiv.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
    }

    function showStats(data) {
        document.getElementById('statsOriginalUrl').textContent = data.originalUrl;
        document.getElementById('statsShortCode').textContent = data.shortCode;
        document.getElementById('statsClickCount').textContent = data.clickCount;
        document.getElementById('statsCreatedAt').textContent = new Date(data.createdAt).toLocaleString();

        statsDiv.classList.remove('hidden');

        // Scroll to stats
        statsDiv.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
    }

    function showMessage(message, type) {
        // Remove existing messages
        const existingMessages = document.querySelectorAll('.error, .success');
        existingMessages.forEach(msg => msg.remove());

        // Create new message
        const messageDiv = document.createElement('div');
        messageDiv.className = type;
        messageDiv.textContent = message;

        // Insert after form
        const form = document.getElementById('urlForm');
        form.parentNode.insertBefore(messageDiv, form.nextSibling);

        // Auto-hide after 5 seconds
        setTimeout(() => {
            messageDiv.remove();
        }, 5000);
    }

    // Custom code validation
    document.getElementById('customCode').addEventListener('input', function(e) {
        const value = e.target.value;
        const regex = /^[A-Za-z0-9]*$/;

        if (!regex.test(value)) {
            e.target.setCustomValidity('Only letters and numbers are allowed');
        } else {
            e.target.setCustomValidity('');
        }
    });

    // URL validation feedback
    document.getElementById('originalUrl').addEventListener('input', function(e) {
        const value = e.target.value;
        const urlRegex = /^https?:\/\/.+/;

        if (value && !urlRegex.test(value)) {
            e.target.setCustomValidity('Please enter a valid URL starting with http:// or https://');
        } else {
            e.target.setCustomValidity('');
        }
    });
});