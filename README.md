# ProjectCS2063
The CS2063 app term project: Fitness App
API version 23 and over
Test on physical device

## Introduction:
In this time of Covid-19, people are staying inside more and have less will to get exercise and stay healthy. Since not everyone has a gym available to them, an easy and universal way to exercise is to just walk around, so it is important that you obtain a certain number of steps in a day! There’s a lot of apps that can offer complex activity monitoring such as FitBit and AppleWatch but these can be costly and overwhelming to a user. We aim to have a simple app that’s easy to use, easy to understand, doesn’t require external devices beyond the app, and can be used without a network connection!

## Brief description: 
A fitness app used to count steps and track water intake! This application’s main purpose is to help the user stay fit. This will be achieved by tracking the user’s steps, obtaining relevant user data, tracking their water intake and sending reminders through push notifications. Overall, the app will serve a useful purpose by helping the user keep track of their fitness and wellbeing. 

## How it works:
Clone the source code, install and run the app. When the app boots up, it will take you to your steps fragment. We have automatically assumed a goal of 10,000 steps and a goal of 2000ml of water based on general recommendations from health professionals. These goals are able to be customized, navigate to the users fragment, here you can change your goals. You can also fill in some identifying information (optional) such as your height, weight and age. Once you install the app, the steps feature is constantly tracking your steps through use of acceleromoeter, even when the app is closed. You can check your progress in the app fragment and if you meet your goals, you will get a congratualtions notification. Our app will recognize when it is the next day based on your internal system clock, and the fragments will reset and the date displayed will chagne. The water feature works much like the steps feature except tracking is done manually. To add water, you must select one of the options and push on the "add" button and likewise for removing water. You can also add or remove your own custom amount. Lastly, you can clear all your data for the day by selecting the "clear" button. While your goal has not been met, there is a notification set out every hour to remind you to drink water. There is also a congratulations otification sent out when a user has acheived their goal. 

## Things to note:
Our app performs step tracking by use of accelerometer. The original plan was to make use of the built- in pedometer/step-counter/step-detector sensor. Unfortunately, we had no access to an android phone that actually had that feature and therefore, no way of testing it to make sure it functioned. Emulated devices cannot trigger the step detector sensor, so please keep that in mind. You will need a physical device to properly test the step detecting feature.  
