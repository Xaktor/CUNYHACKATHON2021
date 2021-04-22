# cook it.

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)
3. [Links](#Links)

CHANGE 

## Overview
### Description
cook it. will allow user to browse recipes. Create a meal plan. Start timers while they are cooking. Create a shopping list. 

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Cooking
- **Mobile:** The app will be developed for mobile. All the functionality will be available on mobile devices. Currently there is no plan for the app to be available on Web of iOS.
- **Story:** App allows users to browse cooking recipes, see the ingeredients needed for a recipe and the instructions. Users can create shopping lists.
- **Market:** Anyone can choose to use this app. 
- **Habit:** The app can be used any time the user wants to cook a meal, browse for a recipe, create a shopping list or go grocery shopping.
- **Scope:** The schope of original features is limitted. Users can use the app but cannot interact with other users. Down the line, user profiles can be integrated into the flow where users can save more about them, their favorite recipes, diet preferences and potentially interact with other users.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* User can browse recipes
* User can start cooking timers
* User can create shopping lists
* 
* ...

**Optional Nice-to-have Stories**

* User can login
* User can register 
* User can search for recipes
* User can favorite recipes
* User can add ingredients from recipe detail to the shopping list
* UI elements (Vector art, loading screens, etc)
* User can access profile with saved recipes
* ...

### 2. Screen Archetypes

* Login [Optional]
   * User can login
* Register [Optional]
    * User can create an account
* Recipe Stream
    * User can browse recipes
    * User can favorite a recipe [Optional]
* Recipe Detail
    * User can see more about a recipe
    * User can add ingredients to the shopping list [Optional]
    * User can favorite a recipe [Optional]
* Timer Fragment
    * User can set a timer while cooking
* Shopping List Fragment
    * User can view all the necessary ingredients

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Recipe Stream
* Shopping List Creation
* Timer
* Search [Optional]
* Profile [Optional]

**Flow Navigation** (Screen to Screen)

* Recipe Stream
   * Detail
   * 
   * ...
* Timer
   * Timer 
* Shopping List
* Login [Optional]
    * Recipe Stream
* Register [Optional]
    * Recipe Stream

## Wireframes
[Add picture of your hand sketched wireframes in this section]
<img src="https://i.imgur.com/WgUzs8U.jpg" width=600>

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 

### Models

Model: Recipe
| Property | Type     | Description |
| -------- | -------- | --------    |
| recipeId | double   | Unique id for the recipe|
| title    | String   | Name of the recipe |
| image    | File     | Picture of the recipe|
| servings | Number   | Number of servings |
| readyInMinutes| double | Cooking time |
| ingredients| List<Ingredient>| List of ingredients|
| instructions | String | Instructions to cook a meal |

Model: Ingredient
| Property | Type     | Description |
| -------- | -------- | -------- |
| name     | String     | Name of an ingredient     |
| amount   | double     | Amount of an ingredient |
| unitShort | String | Abreviated units of an ingredient|



### Networking
- [Add list of network requests by screen ]
1. Recipe Feed Screen
-- (Read/GET) Browse recipes to the feed and details of the recipes.
- [Create basic snippets for each Parse network request]
--Using API.

- [OPTIONAL: List endpoints if using existing API such as Yelp]

- Base URL - https://spoonacular.com/food-api/recipes/complexSearch


| HTTP Verb | Endpoint | Description |
| -------- | -------- | -------- |
| GET      |    /instructionsRequired | ensure the recipes includes instructions   |
|GET       | /maxReadyTime| filter by cook time|
|GET       | /cuisine | filter by specific cuisine| 
| GET      | /diet     | filter by specific diet|
|GET       | /type     | filter by meal type|

### Links
- Spoonacular: https://spoonacular.com/food-api/docs#Search-Recipes-Complex
- Spoonacular: supported cuisines: https://spoonacular.com/food-api/docs#Cuisines
- Spoonacular: supported diets: https://spoonacular.com/food-api/docs#Diets
- Spoonacular: supported meal types: https://spoonacular.com/food-api/docs#Meal-Types
- Previous HackMD doc, app ideas: https://hackmd.io/igNJdjSWQH-fUJo_HwWQRQ

