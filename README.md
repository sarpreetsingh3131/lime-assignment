# How to run
1. Download *jar*
2. Open terminal and write `java -jar lime-assignment-1.0-SNAPSHOT.jar`
---

## API
---
**Find Free Time Slots**
---
* **URL** `http://localhost:8080/api/free-time-slots`
* **Method:** `PUT`
* **Headers:** `application/json`
* **Body:**

        {
	        "participantsIds": [
		        "259939411636051033617118653993975778241",
		        "26960588928303314600744170163590314529",
		        "16039290618087039711713849986170311419"
	        ],
	        "meetingLength": 120,
	        "earliestDateTime": "3/13/2015 8:00:00 AM",
	        "latestDateTime": "3/15/2015 8:00:00 AM",
            "timeZone": "UTC+1"
        }
* **Response:**

        [
            {
                "id": "259939411636051033617118653993975778241",
                "name": "Marilyn Mckeown",
                "timeSlots": [
                    {
                        "startDate": "2015-03-13",
                        "endDate": "2015-03-13",
                        "startTime": "08:00",
                        "endTime": "17:00"
                    },
                    {
                        "startDate": "2015-03-14",
                        "endDate": "2015-03-14"
                        "startTime": "08:00",
                        "endTime": "17:00"
                    },
                    ...
                ]
            },
            ....
        ]
---
