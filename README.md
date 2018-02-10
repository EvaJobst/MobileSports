# Mobile Sports
- Github: https://github.com/EvaJobst/MobileSports
- Team: Eva Jobst & Stefan Schwaighofer

### Work Distribution
| Assignment | Eva | Stefan |
|---|:---:|:---:|
| App with server and step detection | X | X |
| Heart-rate-related extensions | X | |
| Calculation of energy expenditure from heart-rate | | X |
| Respiration rate from HRV data | | X |
| Calculation of fitness, fatigue, performance | X | |
| Step detection method by Jimenez | | X |
| ConnectIQ app | | X |
| Activity classification | | X |
| Website | | X |
| Design | X | |
| Documentation | X | |

### Assignments
| Assignment | MOSProject | Mountaineer | Matlab | Other |
|---|:---:|:---:|:---:|:---:|
| App with server and step detection |  | X | | |
| Heart-rate-related extensions | X | X | | |
| Calculation of energy expenditure from heart-rate | X | X | | |
| Respiration rate from HRV data | | | X | |
| Calculation of fitness, fatigue, performance | X | | | |
| Step detection method by Jimenez | | | X | |
| Equivalent pace; SRTM-corrected altitude or barometric pressure | | | | |
| ConnectIQ app | | | | X |
| Prediction of finishing times | | | | |
| Activity classification | | | | X |

### Locations
#### MOSProject
[Link](https://github.com/EvaJobst/MobileSports/tree/master/src/MOSProject)

##### Step Detection
- Base Directory for Step Detection: [Link](https://github.com/EvaJobst/MobileSports/tree/master/src/MOSProject/app/src/main/java/at/fhooe/mos/app/mosproject/pedometer)

- Class [Pedometer](https://github.com/EvaJobst/MobileSports/blob/master/src/MOSProject/app/src/main/java/at/fhooe/mos/app/mosproject/pedometer/Pedometer.java) is responsible for the actual detection of steps


##### Heart Rate
- Base Directory for Heart Rate related extensions: [Link](https://github.com/EvaJobst/MobileSports/tree/master/src/MOSProject/app/src/main/java/at/fhooe/mos/app/mosproject/heartrate)

- Class [HRCalculation](https://github.com/EvaJobst/MobileSports/blob/master/src/MOSProject/app/src/main/java/at/fhooe/mos/app/mosproject/heartrate/HRCalculation.java) is responsible for:
  - Fitness, Fatigue, Performance
  - HRMax
  - Average Heart Rate
  - Trimp    


- [EnergyExpenditureCalculator](https://github.com/EvaJobst/MobileSports/blob/master/src/MOSProject/app/src/main/java/at/fhooe/mos/app/mosproject/heartrate/EnergyExpenditureCalculator.java) is, as the name says, responsible for calculating kCal out of the messed Heart Rate values

#### Mountaineer
[Link](https://github.com/EvaJobst/MobileSports/tree/master/src/Mountaineer)

##### Server
- Base Directory for Firebase Server implementation: [Link](https://github.com/EvaJobst/MobileSports/tree/master/src/Mountaineer/app/src/main/java/at/fhooe/mos/mountaineer/persistence)

##### Step Detection
- Base Directory for Step Detection: [Link](https://github.com/EvaJobst/MobileSports/tree/master/src/Mountaineer/app/src/main/java/at/fhooe/mos/mountaineer/sensors/stepsensor)

- Class [Pedometer](https://github.com/EvaJobst/MobileSports/blob/master/src/Mountaineer/app/src/main/java/at/fhooe/mos/mountaineer/sensors/stepsensor/Pedometer.java) is responsible for the actual detection of steps

##### Heart Rate
- Base Directory for Heart Rate related extensions: [Link](https://github.com/EvaJobst/MobileSports/tree/master/src/Mountaineer/app/src/main/java/at/fhooe/mos/mountaineer/sensors/heartrate)

- Class [HeartRateCalculation](https://github.com/EvaJobst/MobileSports/blob/master/src/Mountaineer/app/src/main/java/at/fhooe/mos/mountaineer/model/calculations/HeartRateCalculation.java) is responsible for:
  - Fitness, Fatigue, Performance
  - HRMax
  - Average Heart Rate
  - Trimp  


- [EnergyExpenditureCalculator](https://github.com/EvaJobst/MobileSports/blob/master/src/Mountaineer/app/src/main/java/at/fhooe/mos/mountaineer/model/calculations/EnergyExpenditureCalculator.java) is, as the name says, responsible for calculating kCal out of the messed Heart Rate values



#### Website
[Link](https://github.com/EvaJobst/MobileSports/tree/master/src/MountaineerWeb)

#### Matlab
[Link](https://github.com/EvaJobst/MobileSports/tree/master/matlab)

##### Step Detection by Jimenez
[Link](https://github.com/EvaJobst/MobileSports/tree/master/matlab/detectStepsAcceleration)

##### Respiration Rate
[Link](https://github.com/EvaJobst/MobileSports/tree/master/matlab/heartRateVariation)


#### Activity classification
[Link](https://github.com/EvaJobst/MobileSports/tree/master/weka)

#### IQ Connect
[Link](https://github.com/EvaJobst/MobileSports/tree/master/src/Garmin/Stopwatch)

### Demonstration
##### Mountaineer
 <img src="https://github.com/EvaJobst/MobileSports/blob/master/images/gifs/app_demo_small.gif" alt="Demo of App" height="400">

##### Website
![Demo of Website](https://github.com/EvaJobst/MobileSports/blob/master/images/gifs/website_demo.gif)

##### Garmin
![Demo of Garmin](https://github.com/EvaJobst/MobileSports/blob/master/images/garmin_images/button_description.png)
