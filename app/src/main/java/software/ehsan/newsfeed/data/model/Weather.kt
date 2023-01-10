package software.ehsan.newsfeed.data.model

data class Weather(val weather:String) {
}

//https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&daily=temperature_2m_max,temperature_2m_min&timeformat=unixtime&timezone=GMT&current_weather=true