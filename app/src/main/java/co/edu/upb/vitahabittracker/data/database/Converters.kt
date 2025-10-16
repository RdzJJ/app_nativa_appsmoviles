package co.edu.upb.vitahabittracker.data.database

import androidx.room.TypeConverter
import co.edu.upb.vitahabittracker.data.models.HabitFrequency
import co.edu.upb.vitahabittracker.data.models.ReminderFrequency
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Converters {
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? = date?.format(dateFormatter)

    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? =
        dateString?.let { LocalDate.parse(it, dateFormatter) }

    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? = dateTime?.format(dateTimeFormatter)

    @TypeConverter
    fun toLocalDateTime(dateTimeString: String?): LocalDateTime? =
        dateTimeString?.let { LocalDateTime.parse(it, dateTimeFormatter) }

    @TypeConverter
    fun fromHabitFrequency(frequency: HabitFrequency): String = frequency.name

    @TypeConverter
    fun toHabitFrequency(frequency: String): HabitFrequency = HabitFrequency.valueOf(frequency)

    @TypeConverter
    fun fromReminderFrequency(frequency: ReminderFrequency): String = frequency.name

    @TypeConverter
    fun toReminderFrequency(frequency: String): ReminderFrequency = ReminderFrequency.valueOf(frequency)
}
