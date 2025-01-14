# Multilingual chat mitigating toxic messages

## Introduction
This is a server side application of a multilingual chat service which detects and filters toxic messages.

Multilingual feature automatically detects 100+ languages and translate according to each user's preferred language.
This is implemented with Google Cloud's [Cloud Translation - Advanced API](https://cloud.google.com/translate).

A toxic(or disruptive) message which offend other users can be detected and filtered out based on the degree of toxicity of messages.
The feature is implemented with [Perspective API](https://perspectiveapi.com/), which makes it easier to host better conversations.  
The Perspective API also analyze the message and returns several [other attributes](https://support.perspectiveapi.com/s/about-the-api-attributes-and-languages), including 
whether the message is insulting targeted identity, or the message includes profanity words, and so on.

To provide near real-time chat user experience, mobile or frontend applications can connect to this application using WebSocket protocol.  
This application orchestrate all these dependent APIs calls asynchronously and then broadcast final messages to users in chat group.  
The [demo frontend application](https://github.com/google-cloud-japan/m10l-chat-antidote-frontend-demo) integrates the application in this repository, or you can also use one of the browser's WebSocket clients from the [Chrome Web Store](https://chrome.google.com/webstore/search/websocket).


## Preparations

### Setup Google Cloud Project

Google Cloud environment is required to run this application.  
[![Open in Cloud Shell](https://gstatic.com/cloudssh/images/open-btn.png)](https://console.cloud.google.com/)

### Cloud Translation API key

Cloud Translation API should be enabled on your Google Cloud project.  
If you want to run the application on remote or local environment other than Google Cloud environment, 
please follow the setup steps in official [documentation](https://cloud.google.com/translate/docs/setup).

### Perspective API key

The Perspective API is one of the products of the Conversation AI Initiative, 
which is being promoted by the [Jigsaw](https://jigsaw.google.com/) under Alphabet and 
the Google Counter-Abuse Technology team.

You can request for the API key [here](https://support.perspectiveapi.com/s/docs-get-started), and try the sample requests.

### WebSocket client

Please check WebSocket client at [Chrome Web Store](https://chrome.google.com/webstore/search/websocket) to run this application without mobile or frontend application.

## Getting Started

1. Create the new chat group and get a group ID.
```sh
curl --request POST 'http://${SERVER_ADDRESS}/chats/groups' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'name=My Test Room'
```

- List all the chat group.
```shell
curl --location --request GET 'http://${SERVER_ADDRESS}/chats/groups'
```
- Get the chat group detail.
```shell
curl --location --request GET 'http://${SERVER_ADDRESS}/chats/groups/{group_id}'
```


2. Connect to the server `ws://${SERVER_ADDRESS}/ws/chat`.


3. Set the message type `ENTER`, and enter the group with group ID.
```json
{
  "type": "ENTER",
  "text": "",
  "sender": {
    "name": "白井",
    "lang": "ja",
    "photoUrl": "${PHOTO_PROFILE_URL}"
  },
  "groupId": "${CHAT_GROUP_ID}"
}
```

4. To start the conversation in the group, change the message type to `TALK` and send text message.
```json
{
  "type": "TALK",
  "text": "こんにちは。白井です。",
  "sender": {
    "name": "白井",
    "lang": "ja",
    "photoUrl": "${PHOTO_PROFILE_URL}"
  },
  "groupId": "${GROUP_ID}"
}
```

- Sample response with toxicity score for an English user.
```json
[
  {
    "id": "2ebc0285-9d3f-444a-b0be-960ec1f78534",
    "num": 4,
    "lang": "ja",
    "text": "こんにちは。白井です。",
    "type": "TALK",
    "createdAt": "2021-12-21T10:17:45.559943Z",
    "sender": {
      "name": "Shirai",
      "photoUrl": "https://upload.wikimedia.org/wikipedia/commons/9/9a/Gull_portrait_ca_usa.jpg"
    },
    "translations": [
      {
        "lang": "en",
        "text": "Hello. I am Shirai.",
        "toxicityScore": 0.03855397,
        "original": false
      }
    ]
  }
]
```
