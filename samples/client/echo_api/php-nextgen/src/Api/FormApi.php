<?php
/**
 * FormApi
 * PHP version 8.1
 *
 * @package  OpenAPI\Client
 * @author   OpenAPI Generator team
 * @link     https://openapi-generator.tech
 */

/**
 * Echo Server API
 *
 * Echo Server API
 *
 * The version of the OpenAPI document: 0.1.0
 * Contact: team@openapitools.org
 * @generated Generated by: https://openapi-generator.tech
 * OpenAPI Generator version: 8.0.0-SNAPSHOT
 */

/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

namespace OpenAPI\Client\Api;

use InvalidArgumentException;
use GuzzleHttp\Client;
use GuzzleHttp\ClientInterface;
use GuzzleHttp\Exception\ConnectException;
use GuzzleHttp\Exception\RequestException;
use GuzzleHttp\Psr7\MultipartStream;
use GuzzleHttp\Psr7\Request;
use GuzzleHttp\RequestOptions;
use GuzzleHttp\Promise\PromiseInterface;
use OpenAPI\Client\ApiException;
use OpenAPI\Client\Configuration;
use OpenAPI\Client\HeaderSelector;
use OpenAPI\Client\ObjectSerializer;

/**
 * FormApi Class Doc Comment
 *
 * @package  OpenAPI\Client
 * @author   OpenAPI Generator team
 * @link     https://openapi-generator.tech
 */
class FormApi
{
    /**
     * @var ClientInterface
     */
    protected ClientInterface $client;

    /**
     * @var Configuration
     */
    protected Configuration $config;

    /**
     * @var HeaderSelector
     */
    protected HeaderSelector $headerSelector;

    /**
     * @var int Host index
     */
    protected int $hostIndex;

    /** @var string[] $contentTypes **/
    public const contentTypes = [
        'testFormIntegerBooleanString' => [
            'application/x-www-form-urlencoded',
        ],
        'testFormOneof' => [
            'application/x-www-form-urlencoded',
        ],
    ];

    /**
     * @param ClientInterface|null $client
     * @param Configuration|null   $config
     * @param HeaderSelector|null  $selector
     * @param int                  $hostIndex (Optional) host index to select the list of hosts if defined in the OpenAPI spec
     */
    public function __construct(
        ClientInterface $client = null,
        Configuration $config = null,
        HeaderSelector $selector = null,
        int $hostIndex = 0
    ) {
        $this->client = $client ?: new Client();
        $this->config = $config ?: new Configuration();
        $this->headerSelector = $selector ?: new HeaderSelector();
        $this->hostIndex = $hostIndex;
    }

    /**
     * Set the host index
     *
     * @param int $hostIndex Host index (required)
     */
    public function setHostIndex(int $hostIndex): void
    {
        $this->hostIndex = $hostIndex;
    }

    /**
     * Get the host index
     *
     * @return int Host index
     */
    public function getHostIndex(): int
    {
        return $this->hostIndex;
    }

    /**
     * @return Configuration
     */
    public function getConfig(): Configuration
    {
        return $this->config;
    }

    /**
     * Operation testFormIntegerBooleanString
     *
     * Test form parameter(s)
     *
     * @param  int|null $integer_form integer_form (optional)
     * @param  bool|null $boolean_form boolean_form (optional)
     * @param  string|null $string_form string_form (optional)
     * @param  string $contentType The value for the Content-Type header. Check self::contentTypes['testFormIntegerBooleanString'] to see the possible values for this operation
     *
     * @throws ApiException on non-2xx response or if the response body is not in the expected format
     * @throws InvalidArgumentException
     * @return string
     */
    public function testFormIntegerBooleanString(
        ?int $integer_form = null,
        ?bool $boolean_form = null,
        ?string $string_form = null,
        string $contentType = self::contentTypes['testFormIntegerBooleanString'][0]
    ): string
    {
        list($response) = $this->testFormIntegerBooleanStringWithHttpInfo($integer_form, $boolean_form, $string_form, $contentType);
        return $response;
    }

    /**
     * Operation testFormIntegerBooleanStringWithHttpInfo
     *
     * Test form parameter(s)
     *
     * @param  int|null $integer_form (optional)
     * @param  bool|null $boolean_form (optional)
     * @param  string|null $string_form (optional)
     * @param  string $contentType The value for the Content-Type header. Check self::contentTypes['testFormIntegerBooleanString'] to see the possible values for this operation
     *
     * @throws ApiException on non-2xx response or if the response body is not in the expected format
     * @throws InvalidArgumentException
     * @return array of string, HTTP status code, HTTP response headers (array of strings)
     */
    public function testFormIntegerBooleanStringWithHttpInfo(
        ?int $integer_form = null,
        ?bool $boolean_form = null,
        ?string $string_form = null,
        string $contentType = self::contentTypes['testFormIntegerBooleanString'][0]
    ): array
    {
        $request = $this->testFormIntegerBooleanStringRequest($integer_form, $boolean_form, $string_form, $contentType);

        try {
            $options = $this->createHttpClientOption();
            try {
                $response = $this->client->send($request, $options);
            } catch (RequestException $e) {
                throw new ApiException(
                    "[{$e->getCode()}] {$e->getMessage()}",
                    (int) $e->getCode(),
                    $e->getResponse() ? $e->getResponse()->getHeaders() : null,
                    $e->getResponse() ? (string) $e->getResponse()->getBody() : null
                );
            } catch (ConnectException $e) {
                throw new ApiException(
                    "[{$e->getCode()}] {$e->getMessage()}",
                    (int) $e->getCode(),
                    null,
                    null
                );
            }

            $statusCode = $response->getStatusCode();

            if ($statusCode < 200 || $statusCode > 299) {
                throw new ApiException(
                    sprintf(
                        '[%d] Error connecting to the API (%s)',
                        $statusCode,
                        (string) $request->getUri()
                    ),
                    $statusCode,
                    $response->getHeaders(),
                    (string) $response->getBody()
                );
            }

            switch($statusCode) {
                case 200:
                    if ('string' === '\SplFileObject') {
                        $content = $response->getBody(); //stream goes to serializer
                    } else {
                        $content = (string) $response->getBody();
                        if ('string' !== 'string') {
                            try {
                                $content = json_decode($content, false, 512, JSON_THROW_ON_ERROR);
                            } catch (\JsonException $exception) {
                                throw new ApiException(
                                    sprintf(
                                        'Error JSON decoding server response (%s)',
                                        $request->getUri()
                                    ),
                                    $statusCode,
                                    $response->getHeaders(),
                                    $content
                                );
                            }
                        }
                    }

                    return [
                        ObjectSerializer::deserialize($content, 'string', []),
                        $response->getStatusCode(),
                        $response->getHeaders()
                    ];
            }

            $returnType = 'string';
            if ($returnType === '\SplFileObject') {
                $content = $response->getBody(); //stream goes to serializer
            } else {
                $content = (string) $response->getBody();
                if ($returnType !== 'string') {
                    try {
                        $content = json_decode($content, false, 512, JSON_THROW_ON_ERROR);
                    } catch (\JsonException $exception) {
                        throw new ApiException(
                            sprintf(
                                'Error JSON decoding server response (%s)',
                                $request->getUri()
                            ),
                            $statusCode,
                            $response->getHeaders(),
                            $content
                        );
                    }
                }
            }

            return [
                ObjectSerializer::deserialize($content, $returnType, []),
                $response->getStatusCode(),
                $response->getHeaders()
            ];

        } catch (ApiException $e) {
            switch ($e->getCode()) {
                case 200:
                    $data = ObjectSerializer::deserialize(
                        $e->getResponseBody(),
                        'string',
                        $e->getResponseHeaders()
                    );
                    $e->setResponseObject($data);
                    break;
            }
            throw $e;
        }
    }

    /**
     * Operation testFormIntegerBooleanStringAsync
     *
     * Test form parameter(s)
     *
     * @param  int|null $integer_form (optional)
     * @param  bool|null $boolean_form (optional)
     * @param  string|null $string_form (optional)
     * @param  string $contentType The value for the Content-Type header. Check self::contentTypes['testFormIntegerBooleanString'] to see the possible values for this operation
     *
     * @throws InvalidArgumentException
     * @return PromiseInterface
     */
    public function testFormIntegerBooleanStringAsync(
        ?int $integer_form = null,
        ?bool $boolean_form = null,
        ?string $string_form = null,
        string $contentType = self::contentTypes['testFormIntegerBooleanString'][0]
    ): PromiseInterface
    {
        return $this->testFormIntegerBooleanStringAsyncWithHttpInfo($integer_form, $boolean_form, $string_form, $contentType)
            ->then(
                function ($response) {
                    return $response[0];
                }
            );
    }

    /**
     * Operation testFormIntegerBooleanStringAsyncWithHttpInfo
     *
     * Test form parameter(s)
     *
     * @param  int|null $integer_form (optional)
     * @param  bool|null $boolean_form (optional)
     * @param  string|null $string_form (optional)
     * @param  string $contentType The value for the Content-Type header. Check self::contentTypes['testFormIntegerBooleanString'] to see the possible values for this operation
     *
     * @throws InvalidArgumentException
     * @return PromiseInterface
     */
    public function testFormIntegerBooleanStringAsyncWithHttpInfo(
        $integer_form = null,
        $boolean_form = null,
        $string_form = null,
        string $contentType = self::contentTypes['testFormIntegerBooleanString'][0]
    ): PromiseInterface
    {
        $returnType = 'string';
        $request = $this->testFormIntegerBooleanStringRequest($integer_form, $boolean_form, $string_form, $contentType);

        return $this->client
            ->sendAsync($request, $this->createHttpClientOption())
            ->then(
                function ($response) use ($returnType) {
                    if ($returnType === '\SplFileObject') {
                        $content = $response->getBody(); //stream goes to serializer
                    } else {
                        $content = (string) $response->getBody();
                        if ($returnType !== 'string') {
                            $content = json_decode($content);
                        }
                    }

                    return [
                        ObjectSerializer::deserialize($content, $returnType, []),
                        $response->getStatusCode(),
                        $response->getHeaders()
                    ];
                },
                function ($exception) {
                    $response = $exception->getResponse();
                    $statusCode = $response->getStatusCode();
                    throw new ApiException(
                        sprintf(
                            '[%d] Error connecting to the API (%s)',
                            $statusCode,
                            $exception->getRequest()->getUri()
                        ),
                        $statusCode,
                        $response->getHeaders(),
                        (string) $response->getBody()
                    );
                }
            );
    }

    /**
     * Create request for operation 'testFormIntegerBooleanString'
     *
     * @param  int|null $integer_form (optional)
     * @param  bool|null $boolean_form (optional)
     * @param  string|null $string_form (optional)
     * @param  string $contentType The value for the Content-Type header. Check self::contentTypes['testFormIntegerBooleanString'] to see the possible values for this operation
     *
     * @throws InvalidArgumentException
     * @return \GuzzleHttp\Psr7\Request
     */
    public function testFormIntegerBooleanStringRequest(
        $integer_form = null,
        $boolean_form = null,
        $string_form = null,
        string $contentType = self::contentTypes['testFormIntegerBooleanString'][0]
    ): Request
    {





        $resourcePath = '/form/integer/boolean/string';
        $formParams = [];
        $queryParams = [];
        $headerParams = [];
        $httpBody = '';
        $multipart = false;




        // form params
        if ($integer_form !== null) {
            $formParams['integer_form'] = ObjectSerializer::toFormValue($integer_form);
        }
        // form params
        if ($boolean_form !== null) {
            $formParams['boolean_form'] = ObjectSerializer::toFormValue($boolean_form);
        }
        // form params
        if ($string_form !== null) {
            $formParams['string_form'] = ObjectSerializer::toFormValue($string_form);
        }

        $headers = $this->headerSelector->selectHeaders(
            ['text/plain', ],
            $contentType,
            $multipart
        );

        // for model (json/xml)
        if (count($formParams) > 0) {
            if ($multipart) {
                $multipartContents = [];
                foreach ($formParams as $formParamName => $formParamValue) {
                    $formParamValueItems = is_array($formParamValue) ? $formParamValue : [$formParamValue];
                    foreach ($formParamValueItems as $formParamValueItem) {
                        $multipartContents[] = [
                            'name' => $formParamName,
                            'contents' => $formParamValueItem
                        ];
                    }
                }
                // for HTTP post (form)
                $httpBody = new MultipartStream($multipartContents);

            } elseif (stripos($headers['Content-Type'], 'application/json') !== false) {
                # if Content-Type contains "application/json", json_encode the form parameters
                $httpBody = \GuzzleHttp\Utils::jsonEncode($formParams);
            } else {
                // for HTTP post (form)
                $httpBody = ObjectSerializer::buildQuery($formParams);
            }
        }


        $defaultHeaders = [];
        if ($this->config->getUserAgent()) {
            $defaultHeaders['User-Agent'] = $this->config->getUserAgent();
        }

        $headers = array_merge(
            $defaultHeaders,
            $headerParams,
            $headers
        );

        $operationHost = $this->config->getHost();
        $query = ObjectSerializer::buildQuery($queryParams);
        return new Request(
            'POST',
            $operationHost . $resourcePath . ($query ? "?{$query}" : ''),
            $headers,
            $httpBody
        );
    }

    /**
     * Operation testFormOneof
     *
     * Test form parameter(s) for oneOf schema
     *
     * @param  string|null $form1 form1 (optional)
     * @param  int|null $form2 form2 (optional)
     * @param  string|null $form3 form3 (optional)
     * @param  bool|null $form4 form4 (optional)
     * @param  int|null $id id (optional)
     * @param  string|null $name name (optional)
     * @param  string $contentType The value for the Content-Type header. Check self::contentTypes['testFormOneof'] to see the possible values for this operation
     *
     * @throws ApiException on non-2xx response or if the response body is not in the expected format
     * @throws InvalidArgumentException
     * @return string
     */
    public function testFormOneof(
        ?string $form1 = null,
        ?int $form2 = null,
        ?string $form3 = null,
        ?bool $form4 = null,
        ?int $id = null,
        ?string $name = null,
        string $contentType = self::contentTypes['testFormOneof'][0]
    ): string
    {
        list($response) = $this->testFormOneofWithHttpInfo($form1, $form2, $form3, $form4, $id, $name, $contentType);
        return $response;
    }

    /**
     * Operation testFormOneofWithHttpInfo
     *
     * Test form parameter(s) for oneOf schema
     *
     * @param  string|null $form1 (optional)
     * @param  int|null $form2 (optional)
     * @param  string|null $form3 (optional)
     * @param  bool|null $form4 (optional)
     * @param  int|null $id (optional)
     * @param  string|null $name (optional)
     * @param  string $contentType The value for the Content-Type header. Check self::contentTypes['testFormOneof'] to see the possible values for this operation
     *
     * @throws ApiException on non-2xx response or if the response body is not in the expected format
     * @throws InvalidArgumentException
     * @return array of string, HTTP status code, HTTP response headers (array of strings)
     */
    public function testFormOneofWithHttpInfo(
        ?string $form1 = null,
        ?int $form2 = null,
        ?string $form3 = null,
        ?bool $form4 = null,
        ?int $id = null,
        ?string $name = null,
        string $contentType = self::contentTypes['testFormOneof'][0]
    ): array
    {
        $request = $this->testFormOneofRequest($form1, $form2, $form3, $form4, $id, $name, $contentType);

        try {
            $options = $this->createHttpClientOption();
            try {
                $response = $this->client->send($request, $options);
            } catch (RequestException $e) {
                throw new ApiException(
                    "[{$e->getCode()}] {$e->getMessage()}",
                    (int) $e->getCode(),
                    $e->getResponse() ? $e->getResponse()->getHeaders() : null,
                    $e->getResponse() ? (string) $e->getResponse()->getBody() : null
                );
            } catch (ConnectException $e) {
                throw new ApiException(
                    "[{$e->getCode()}] {$e->getMessage()}",
                    (int) $e->getCode(),
                    null,
                    null
                );
            }

            $statusCode = $response->getStatusCode();

            if ($statusCode < 200 || $statusCode > 299) {
                throw new ApiException(
                    sprintf(
                        '[%d] Error connecting to the API (%s)',
                        $statusCode,
                        (string) $request->getUri()
                    ),
                    $statusCode,
                    $response->getHeaders(),
                    (string) $response->getBody()
                );
            }

            switch($statusCode) {
                case 200:
                    if ('string' === '\SplFileObject') {
                        $content = $response->getBody(); //stream goes to serializer
                    } else {
                        $content = (string) $response->getBody();
                        if ('string' !== 'string') {
                            try {
                                $content = json_decode($content, false, 512, JSON_THROW_ON_ERROR);
                            } catch (\JsonException $exception) {
                                throw new ApiException(
                                    sprintf(
                                        'Error JSON decoding server response (%s)',
                                        $request->getUri()
                                    ),
                                    $statusCode,
                                    $response->getHeaders(),
                                    $content
                                );
                            }
                        }
                    }

                    return [
                        ObjectSerializer::deserialize($content, 'string', []),
                        $response->getStatusCode(),
                        $response->getHeaders()
                    ];
            }

            $returnType = 'string';
            if ($returnType === '\SplFileObject') {
                $content = $response->getBody(); //stream goes to serializer
            } else {
                $content = (string) $response->getBody();
                if ($returnType !== 'string') {
                    try {
                        $content = json_decode($content, false, 512, JSON_THROW_ON_ERROR);
                    } catch (\JsonException $exception) {
                        throw new ApiException(
                            sprintf(
                                'Error JSON decoding server response (%s)',
                                $request->getUri()
                            ),
                            $statusCode,
                            $response->getHeaders(),
                            $content
                        );
                    }
                }
            }

            return [
                ObjectSerializer::deserialize($content, $returnType, []),
                $response->getStatusCode(),
                $response->getHeaders()
            ];

        } catch (ApiException $e) {
            switch ($e->getCode()) {
                case 200:
                    $data = ObjectSerializer::deserialize(
                        $e->getResponseBody(),
                        'string',
                        $e->getResponseHeaders()
                    );
                    $e->setResponseObject($data);
                    break;
            }
            throw $e;
        }
    }

    /**
     * Operation testFormOneofAsync
     *
     * Test form parameter(s) for oneOf schema
     *
     * @param  string|null $form1 (optional)
     * @param  int|null $form2 (optional)
     * @param  string|null $form3 (optional)
     * @param  bool|null $form4 (optional)
     * @param  int|null $id (optional)
     * @param  string|null $name (optional)
     * @param  string $contentType The value for the Content-Type header. Check self::contentTypes['testFormOneof'] to see the possible values for this operation
     *
     * @throws InvalidArgumentException
     * @return PromiseInterface
     */
    public function testFormOneofAsync(
        ?string $form1 = null,
        ?int $form2 = null,
        ?string $form3 = null,
        ?bool $form4 = null,
        ?int $id = null,
        ?string $name = null,
        string $contentType = self::contentTypes['testFormOneof'][0]
    ): PromiseInterface
    {
        return $this->testFormOneofAsyncWithHttpInfo($form1, $form2, $form3, $form4, $id, $name, $contentType)
            ->then(
                function ($response) {
                    return $response[0];
                }
            );
    }

    /**
     * Operation testFormOneofAsyncWithHttpInfo
     *
     * Test form parameter(s) for oneOf schema
     *
     * @param  string|null $form1 (optional)
     * @param  int|null $form2 (optional)
     * @param  string|null $form3 (optional)
     * @param  bool|null $form4 (optional)
     * @param  int|null $id (optional)
     * @param  string|null $name (optional)
     * @param  string $contentType The value for the Content-Type header. Check self::contentTypes['testFormOneof'] to see the possible values for this operation
     *
     * @throws InvalidArgumentException
     * @return PromiseInterface
     */
    public function testFormOneofAsyncWithHttpInfo(
        $form1 = null,
        $form2 = null,
        $form3 = null,
        $form4 = null,
        $id = null,
        $name = null,
        string $contentType = self::contentTypes['testFormOneof'][0]
    ): PromiseInterface
    {
        $returnType = 'string';
        $request = $this->testFormOneofRequest($form1, $form2, $form3, $form4, $id, $name, $contentType);

        return $this->client
            ->sendAsync($request, $this->createHttpClientOption())
            ->then(
                function ($response) use ($returnType) {
                    if ($returnType === '\SplFileObject') {
                        $content = $response->getBody(); //stream goes to serializer
                    } else {
                        $content = (string) $response->getBody();
                        if ($returnType !== 'string') {
                            $content = json_decode($content);
                        }
                    }

                    return [
                        ObjectSerializer::deserialize($content, $returnType, []),
                        $response->getStatusCode(),
                        $response->getHeaders()
                    ];
                },
                function ($exception) {
                    $response = $exception->getResponse();
                    $statusCode = $response->getStatusCode();
                    throw new ApiException(
                        sprintf(
                            '[%d] Error connecting to the API (%s)',
                            $statusCode,
                            $exception->getRequest()->getUri()
                        ),
                        $statusCode,
                        $response->getHeaders(),
                        (string) $response->getBody()
                    );
                }
            );
    }

    /**
     * Create request for operation 'testFormOneof'
     *
     * @param  string|null $form1 (optional)
     * @param  int|null $form2 (optional)
     * @param  string|null $form3 (optional)
     * @param  bool|null $form4 (optional)
     * @param  int|null $id (optional)
     * @param  string|null $name (optional)
     * @param  string $contentType The value for the Content-Type header. Check self::contentTypes['testFormOneof'] to see the possible values for this operation
     *
     * @throws InvalidArgumentException
     * @return \GuzzleHttp\Psr7\Request
     */
    public function testFormOneofRequest(
        $form1 = null,
        $form2 = null,
        $form3 = null,
        $form4 = null,
        $id = null,
        $name = null,
        string $contentType = self::contentTypes['testFormOneof'][0]
    ): Request
    {








        $resourcePath = '/form/oneof';
        $formParams = [];
        $queryParams = [];
        $headerParams = [];
        $httpBody = '';
        $multipart = false;




        // form params
        if ($form1 !== null) {
            $formParams['form1'] = ObjectSerializer::toFormValue($form1);
        }
        // form params
        if ($form2 !== null) {
            $formParams['form2'] = ObjectSerializer::toFormValue($form2);
        }
        // form params
        if ($form3 !== null) {
            $formParams['form3'] = ObjectSerializer::toFormValue($form3);
        }
        // form params
        if ($form4 !== null) {
            $formParams['form4'] = ObjectSerializer::toFormValue($form4);
        }
        // form params
        if ($id !== null) {
            $formParams['id'] = ObjectSerializer::toFormValue($id);
        }
        // form params
        if ($name !== null) {
            $formParams['name'] = ObjectSerializer::toFormValue($name);
        }

        $headers = $this->headerSelector->selectHeaders(
            ['text/plain', ],
            $contentType,
            $multipart
        );

        // for model (json/xml)
        if (count($formParams) > 0) {
            if ($multipart) {
                $multipartContents = [];
                foreach ($formParams as $formParamName => $formParamValue) {
                    $formParamValueItems = is_array($formParamValue) ? $formParamValue : [$formParamValue];
                    foreach ($formParamValueItems as $formParamValueItem) {
                        $multipartContents[] = [
                            'name' => $formParamName,
                            'contents' => $formParamValueItem
                        ];
                    }
                }
                // for HTTP post (form)
                $httpBody = new MultipartStream($multipartContents);

            } elseif (stripos($headers['Content-Type'], 'application/json') !== false) {
                # if Content-Type contains "application/json", json_encode the form parameters
                $httpBody = \GuzzleHttp\Utils::jsonEncode($formParams);
            } else {
                // for HTTP post (form)
                $httpBody = ObjectSerializer::buildQuery($formParams);
            }
        }


        $defaultHeaders = [];
        if ($this->config->getUserAgent()) {
            $defaultHeaders['User-Agent'] = $this->config->getUserAgent();
        }

        $headers = array_merge(
            $defaultHeaders,
            $headerParams,
            $headers
        );

        $operationHost = $this->config->getHost();
        $query = ObjectSerializer::buildQuery($queryParams);
        return new Request(
            'POST',
            $operationHost . $resourcePath . ($query ? "?{$query}" : ''),
            $headers,
            $httpBody
        );
    }

    /**
     * Create http client option
     *
     * @throws \RuntimeException on file opening failure
     * @return array of http client options
     */
    protected function createHttpClientOption(): array
    {
        $options = [];
        if ($this->config->getDebug()) {
            $options[RequestOptions::DEBUG] = fopen($this->config->getDebugFile(), 'a');
            if (!$options[RequestOptions::DEBUG]) {
                throw new \RuntimeException('Failed to open the debug file: ' . $this->config->getDebugFile());
            }
        }

        return $options;
    }
}
