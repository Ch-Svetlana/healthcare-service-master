package ru.netology.patient.service.medical;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.alert.SendAlertServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MedicalServiceImplTest {


    @Test
    public void checkBloodPressureTest () throws Exception{
        String ID = "123";
        BloodPressure bp = new BloodPressure(110, 80);

        PatientInfoFileRepository patientInfo = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfo.getById(ID))
                .thenReturn(new PatientInfo(ID, "Иван", "Петров", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.74"), new BloodPressure(120, 80))));

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        SendAlertService sendAlert = Mockito.mock(SendAlertServiceImpl.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfo, sendAlert);
        medicalService.checkBloodPressure(ID, bp);
        Mockito.verify(sendAlert).send(argumentCaptor.capture());
        Assertions.assertEquals("Warning, patient with id: 123, need help", argumentCaptor.getValue());

    }

    @Test

    public void checkTemperatureTest () throws Exception{
        String ID = "123";
        BigDecimal temperature = BigDecimal.valueOf(36.6);

        PatientInfoFileRepository patientInfo = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfo.getById(ID))
                .thenReturn(new PatientInfo(ID, "Иван", "Петров", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("40"), new BloodPressure(120, 80))));

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        SendAlertService sendAlert = Mockito.mock(SendAlertServiceImpl.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfo, sendAlert);
        medicalService.checkTemperature(ID, temperature);
        Mockito.verify(sendAlert).send(argumentCaptor.capture());
        Assertions.assertEquals("Warning, patient with id: 123, need help", argumentCaptor.getValue());
    }

    @Test

    public void notSendTemperature () throws Exception{
        String ID = "123";
        BigDecimal temperature = BigDecimal.valueOf(36.6);
        PatientInfoFileRepository patientInfo = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfo.getById(ID))
                .thenReturn(new PatientInfo(ID, "Иван", "Петров", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.6"), new BloodPressure(120, 80))));

        SendAlertService sendAlert = Mockito.mock(SendAlertServiceImpl.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfo, sendAlert);
        medicalService.checkTemperature(ID, temperature);

        Mockito.verify(sendAlert, Mockito.times(0)).send(Mockito.anyString());

    }

    @Test
    public void notSendBloodPressure () throws Exception{
        String ID = "123";
        BloodPressure bp = new BloodPressure(120, 80);
        PatientInfoFileRepository patientInfo = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfo.getById(ID))
                .thenReturn(new PatientInfo(ID, "Иван", "Петров", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.6"), new BloodPressure(120, 80))));

        SendAlertService sendAlert = Mockito.mock(SendAlertServiceImpl.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfo, sendAlert);
        medicalService.checkBloodPressure(ID, bp);
        Mockito.verify(sendAlert, Mockito.times(0)).send(Mockito.anyString());

    }

    

}
