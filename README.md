This is a tool to collect nested data from JSON APIs.

# Configuration

## Application Properties
The `application.properties` file defines application runtime settings. Create this file in the current working directory when necessary.

### `settings.file`
**Default**: `message-settings.xml`

Path to the XML file of [message settings](#message-settings).

### `runtime.delay`
**Type**: `integer`

**Default**: `1000`

Runtime delay in milliseconds to check for idle processes.

### `output.allowDuplicate`
**Type**: `boolean`

**Default**: `false`

Whether to allow duplicate output entries.

### `output.trimmed`
**Type**: `boolean`

**Default**: `true`

Whether to trim whitespace from output values.

### `output.dir`
**Default**: `out`

Directory to store output files.

### `connection.repeatOnError.max`
**Type**: `integer`

**Default**: `2`

Maximum number of retry attempts on connection failure.

### `connection.responseCode.pattern`
**Default**: `200`

Regular expression pattern to match HTTP response codes for successful requests.

## Message Settings
The `message-settings.xml` file defines message configurations for querying external APIs. This file **must exist** in the current working directory.

### Structure
```xml
<message-settings>
  <messages>
    <message>
      <name>(string)</name>
      <request>
        <url>(evaluable-string)</url>
        [<method>(http-method)</method>]
        [<params>
          <param key="(string)">(evaluable-string)</param>
          ...
        </params>]
      </request>
      <response>
        [<data-source>(string)</data-source>]
        <values>
          <value>(evaluable-string)</value>
          ...
        </values>
      </response>
    </message>
    ...
  </messages>
</message-settings>
```

### `data-wrapper`
Wrap message response data, contains following variables:

`parent`: Parent data-wrapper from previous message response, `null` at first message.

`data`: Data of the current message response.

### `evaluable-string`
A string that can be evaluated using [Apache Velocity](https://velocity.apache.org/) with access to variables from the [data-wrapper](#data-wrapper).

### `http-method`
An enum value: `GET` or `POST`.

### `message-settings`
#### Fields
**messages** (`message[]`): A list of message definitions, executed in order.

### `message`
#### Fields
**name** (`string`): Message unique identifier, also used as the output filename.

**request** (`request`): Request configuration.

response** (`response`): Response configuration.

### `request`
#### Fields
**url** (`evaluable-string`): The request URL.

**method** (`http-method`, optional): HTTP method. Defaults to `GET`.

**params** (`param[]`, optional): List of HTTP params.

### `param`
#### Attributes
**key** (`string`): Parameter key.

#### Value
(`evaluable-string`): Parameter value.

### `response`
#### Fields
**data-source** (string, optional): Property path as data source of values, must be a reference to array.

**values** (`value[]`): List of output values.

### `value`
#### Value
(`evaluable-string`): Evaluated value.

## Message Settings - Example
### Settings
```xml
<message-settings>
  <messages>
    <message>
      <name>parent</name>
      <request>
        <url>https://example.com/parents</url>
      </request>
      <response>
        <data-source>data</data-source>
        <values>
          <value>${data.id}</value>
          <value>${data.name}</value>
        </values>
      </response>
    </message>
    <message>
      <name>child</name>
      <request>
        <url>https://example.com/parents/${parent.data.id}/children</url>
      </request>
      <response>
        <data-source>data</data-source>
        <values>
          <value>${parent.data.id}</value>
          <value>${data.id}</value>
          <value>${data.name}</value>
        </values>
      </response>
    </message>
  </messages>
</message-settings>
```

### Executed Messages
**URL**: `https://example.com/parents`
```json
{
  "data": [
    { "id": 1, "name": "Parent 1" },
    { "id": 2, "name": "Parent 2" }
  ]
}
```

**URL**: `https://example.com/parents/1/children`
```json
{
  "data": [
    { "id": 11, "name": "Child 1.1" },
    { "id": 12, "name": "Child 1.2" }
  ]
}
```

**URL**: `https://example.com/parents/2/children`
```json
{
  "data": [
    { "id": 21, "name": "Child 2.1" },
    { "id": 22, "name": "Child 2.2" },
    { "id": 23, "name": "Child 2.3" }
  ]
}
```

### Output
**File**: `parent.csv`
```csv
1,Parent 1
2,Parent 2

```

**File**: `child.csv`
```csv
1,11,Child 1.1
1,12,Child 1.2
2,21,Child 2.1
2,22,Child 2.2
2,23,Child 2.3

```
