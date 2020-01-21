# [SW dept] 2019 - Mobile Programming

## **1. The title**
### Battle Neck
![battleNeck](https://postfiles.pstatic.net/MjAxOTA0MTNfMTM4/MDAxNTU1MTU0MzExMTg0.1jafz5v9X67dbpT50AEPJaoBOhahaLjGLB7GmbFuOfYg.I8pCADOIow5fzZpXvN3PEP3IE8c-_tMznLSYY6W14i8g.PNG.tree9295/appdesign.PNG?type=w580)

## **2. Main goal**
There are two problems that arise from using a cell phone in the wrong posture.
1. Text neck
- We want to solve the problem of text neck (Turtle neck syndrome) caused by misuse of smartphone.
2. Visibility & Accident
- Leaning your head down and concentrating on your cell phone is a very big challenge in securing visibility. Our application will help to ensure visibility by changing to the correct posture and help to reduce the accident rate.

## **3. Brief description of project**
>* The battle neck checks the data of the smartphone gyro sensor of the user and if the smartphone is used in the wrong posture, it blocks the screen of the smartphone and induces the user to use the smartphone in the correct posture.
>* In order to motivate users to continue to use, we will introduce a ranking system to the application. It will help people keep their right posture by making them compete and rank.

## **4. Contents**
![contents](https://postfiles.pstatic.net/MjAxOTA0MTNfMTY0/MDAxNTU1MTUzNzExNzc0.35Vrp3UvDkmZiY6q9dVZhoCaNcd4ieX77pJ3-m2dMY0g.dQm7VdSUjlctoJTnEgzODeWnxU8mlYeXx-nJk9A00K8g.PNG.tree9295/123.PNG?type=w580)

1. The user first creates an emoji with his or her own picture.
2. When you use your phone in a bad position, you can't see the screen of your smartphone.
3. If you use your phone in a good position, the point will be saved in the battle neck app as a reward for the position.
4. If you accumulate points, your rankings will rise and you will be able to compete with people.

## **5. Used technology for implementation**

### Image processing technique
##### - Using camera, face recognition and you can use own face character

### Gyro sensor manipulating technique
##### - By using the gyro sensor, motion is recognized through the rotation angle and tilt of the rotating object

### Blurred screen technique
##### - Blur effect that can apply on Camera and UI. Gaussian weights was taken from object by using SuperBlur and SuperBlurFast.

### Database for ranking system
##### - In order to introduce a ranking system for giving motivation, a database is needed to continuously store people's correct postural time.

## **6. Key features**
![Hello](https://postfiles.pstatic.net/MjAxOTA0MTNfMTky/MDAxNTU1MTU0MTAwNjQ0.nJCew8lrS4xK-OgzeQ0_24IcUAtOsYrKsiCfcEBYmrIg.fNjuMHUHCNWipr0Dpst-fgubZzgoXdq26MUvAHI3b_Qg.PNG.tree9295/123213.PNG?type=w580)

### Blocking screen
##### - If you use your cell phone in an incorrect posture, block the screen until you are in the correct posture.

### Motivation Ranking
##### - Introducing a ranking system to drive the user's motivation

### Familiar UI
##### - When using a phone in an incorrect posture, our app can tells the condition through a character

## **7. Difference between other apps**
![differ](https://postfiles.pstatic.net/MjAxOTA0MTRfMTUw/MDAxNTU1MjExMDM1MDQ5.n2eqd2GugclIrShRvTGnsLztDXY1VZucNQChUPBp31Qg.45Cm-Yf7PlREoNjSzAC54B0v2nBn801fnDMivOiotVAg.PNG.tree9295/123.PNG?type=w580)
##### - They are not easy to motivate users. So, easy to delete.

## **8. Member information & Timeline**

#### Hwang byunghoon
[hig97@naver.com]
GUI(XML) development, Check the open source

#### Kim bobae
[tree9295@gmail.com]
Android(JAVA) development, Review the idea, Technicalization

#### Kang beonghyun
[zxcvb8802@naver.com]
Check necessary techniqueSuggest idea, Technicalization, Documentation

#### Timeline

![timeline](https://postfiles.pstatic.net/MjAxOTA0MDhfOSAg/MDAxNTU0NjU5Njg3MzI1.fpTkMv8NAMMnzNJ7UTAO2E0JUFqv4067NnyvPT81Fysg.iahLK4IxNrNNDNu-qMOko5JlY12uHdGrmUKQ1_ItIJUg.PNG.tree9295/%ED%83%80%EC%9E%84%EB%9D%BC%EC%9D%B8.PNG?type=w580)
