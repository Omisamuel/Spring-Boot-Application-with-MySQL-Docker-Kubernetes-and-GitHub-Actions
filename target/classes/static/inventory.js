// inventory.js

const apiBaseUrl = 'http://localhost:8090/api/products';

const displayResults = (data) => {
    const formattedData = JSON.stringify(data, null, 2);
    const highlightedData = `<pre>${formattedData.replace(/("[^"]*":\s*".*?")/g, '<span class="text-primary">$1</span>')}</pre>`;
    $('#resultOutput').html(highlightedData);
};

const handleError = (error) => {
    const errorMessage = error.message ? `${error.status} - ${error.message}` : 'Unexpected error occurred';
    $('#resultOutput').html(`<div class="alert alert-danger">${errorMessage}</div>`);
    console.error('Error details:', error);
};

const showLoading = () => {
    $('#resultOutput').html('<div class="spinner-border text-primary" role="status"><span class="visually-hidden">Loading...</span></div>');
};

const fetchData = async (url, params = {}) => {
    showLoading();
    try {
        const query = new URLSearchParams(params).toString();
        const response = await fetch(`${url}?${query}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        displayResults(data);
    } catch (error) {
        handleError(error);
    }
};

const postData = async (url, data) => {
    showLoading();
    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const responseData = await response.json();
        displayResults(responseData);
    } catch (error) {
        handleError(error);
    }
};

const setupEventListeners = () => {
    $('#viewAllProductsBtn').click(() => fetchData(apiBaseUrl));

    $('#searchForm').submit((event) => {
        event.preventDefault();
        const keyword = $('#keyword').val().trim();
        if (keyword) {
            fetchData(`${apiBaseUrl}/search`, { keyword });
        }
    });

    $('#categoryForm').submit((event) => {
        event.preventDefault();
        const category = $('#category').val().trim();
        if (category) {
            fetchData(`${apiBaseUrl}/category/${encodeURIComponent(category)}`);
        }
    });

    $('#stockForm').submit((event) => {
        event.preventDefault();
        const stock = $('#stock').val();
        if (stock && !isNaN(stock)) {
            fetchData(`${apiBaseUrl}/stock/${stock}`);
        }
    });

    $('#priceForm').submit((event) => {
        event.preventDefault();
        const minPrice = $('#minPrice').val();
        const maxPrice = $('#maxPrice').val();
        if (minPrice && maxPrice && !isNaN(minPrice) && !isNaN(maxPrice)) {
            fetchData(`${apiBaseUrl}/price`, { minPrice, maxPrice });
        }
    });

    $('#addProductForm').submit((event) => {
        event.preventDefault();
        const newProduct = {
            name: $('#productName').val().trim(),
            category: $('#productCategory').val().trim(),
            quantity: parseInt($('#productQuantity').val().trim()),
            stock: parseInt($('#productStock').val().trim()),
            price: parseFloat($('#productPrice').val().trim())
        };
        postData(apiBaseUrl, newProduct);
    });
};

$(document).ready(setupEventListeners);
