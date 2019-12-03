const FETCH_APP = (() => {
    'use strict';

    const FetchApi = () => {
        const GET = 'GET';
        const POST = 'POST';
        const PUT = 'PUT';
        const DELETE = 'DELETE';

        const fetchTemplate = (requestUrl, method, headers, body, ifSucceed) => {
            fetch(requestUrl, {
                method,
                headers,
                body
            }).then(response => {
                if (response.status === 200) {
                    return ifSucceed(response);
                }
                if (response.status === 400) {
                    errorHandler(response);
                }
            });
        };

        const fetchTemplateWithoutBody = (requestUrl, method, ifSucceed) => {
            fetch(requestUrl, {
                method,
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8',
                    'Accept': 'application/json'
                }
            }).then(response => {
                if (response.status === 200) {
                    return ifSucceed(response);
                }
                if (response.status === 400) {
                    errorHandler(response);
                }
            });
        };

        const errorHandler = error => {
            error.json()
                .then(exception => {
                    alert(exception.message)
                });
        };

        return {
            GET,
            POST,
            PUT,
            DELETE,
            fetchTemplate,
            fetchTemplateWithoutBody,
        }
    };

    return {
        FetchApi
    }
})();