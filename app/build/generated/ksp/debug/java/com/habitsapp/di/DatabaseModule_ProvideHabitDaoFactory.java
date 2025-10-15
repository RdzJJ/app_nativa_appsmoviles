package com.habitsapp.di;

import com.habitsapp.data.database.HabitDao;
import com.habitsapp.data.database.HabitsDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class DatabaseModule_ProvideHabitDaoFactory implements Factory<HabitDao> {
  private final Provider<HabitsDatabase> databaseProvider;

  public DatabaseModule_ProvideHabitDaoFactory(Provider<HabitsDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public HabitDao get() {
    return provideHabitDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideHabitDaoFactory create(
      Provider<HabitsDatabase> databaseProvider) {
    return new DatabaseModule_ProvideHabitDaoFactory(databaseProvider);
  }

  public static HabitDao provideHabitDao(HabitsDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideHabitDao(database));
  }
}
