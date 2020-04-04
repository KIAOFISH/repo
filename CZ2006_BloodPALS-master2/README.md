# BloodPALS Source Code
CZ2006 Software Engineering Lab

## Team Alpha
+ Chan Han Wei
+ Htin Kyaw
+ Lusi
+ Muhammad Azharuddin Bin Azhar
+ Tual Sian Muan

## Software
+ Android Studio 
+ Test Virtual Device: Pixel_3_XL_API_27

## GIT Developer Guide

### First Time Checkout from GIT
1. Create folder somewhere in your machine
2. Clone this GIT repository to the folder created ```git clone <repository-url> .```

### View List of Branch
```git branch -a```

### Create Branch or Switch Branch 
```git checkout -b <branch-name>```. Example ```git checkout -b azharuddin```

NOTE:  
+ master is the main branch
+ do not to commit directly to the master branch, but instead create new branch.

### Push Branch to Remote GIT
```git push origin <branch-name>```

## Source Code Guide

### Architecture
3 tier layered architecture.
+ presentation
+ logic
+ data

NOTE: layer component can only communicate with the components on the same layer or the layer directly below it.

### Pattern
+ MVVM Pattern
+ Dependency Injection (Dagger 2)
+ RxJava 
   
### UI
+ Create Fragment and ViewModel. Sample can be found at com.teamalpha.bloodpals.presentation.sample package
+ Add fragment created to the di fragment module (com.teamalpha.bloodpals.di.main.MainFragmentBuildersModule)
+ Add view model created to the di view model module (com.teamalpha.bloodpals.di.main.MainViewModelsModule)
+ Create resource layout at the res layout package (E.g. fragment_quiz.xml)
+ Add layout to the mobile_navigation.xml at res navigation package

#### Navigation Drawer Item
+ Add item to activity_main_drawer.xml at res menu package
+ Go to com.teamalpha.bloodpals.presentation.MainActivity at the function "initUI", add the nav item id to the AppBarConfiguration.Builder
