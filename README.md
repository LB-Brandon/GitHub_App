# GitHub App

<img src="https://github.com/LB-Brandon/GitHub_App/assets/84883277/c26b4b76-3ea2-4b4d-a584-2be65f17beb1" width="200" height="400"/>

# Overview
This project aims to facilitate user searches by allowing them to input a username. Upon entering the desired username, the system displays a list of users containing or matching the input. Users can click on a specific entry in the list to view the repository information associated with that users in a web view.

## What I learned
- Retrofit
- GitHub Open API
- RecyclerView using ListAdpater
- Handler

## Key Function
To enhance the user experience and prevent exceeding the API call limit per minute, the following features were implemented:
- Debouncing applied to the EditText's addTextChangedListener using a Handler to prevent exceeding the API call limit per minute.
- When updating RecyclerView data with a scroll Listener, there was a issue of multiple API calls, which I resolved by implementing the isLoading status.
- Created Data Transfer Object(DTO) for efficient utilization of API data.

## Troubleshooting

#### RecyclerView Pagination Issue
Error: Page jumps occurred only during the second API call when loading the next page.  
Action: The page value was being sent to the API service as a query parameter, initialized to 0; however, it should have started from 1.  
