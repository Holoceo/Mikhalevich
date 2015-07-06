package com.redmadintern.mikhalevich.utils;

import com.redmadintern.mikhalevich.model.local.EventReport;
import com.redmadintern.mikhalevich.model.local.EventStatus;
import com.redmadintern.mikhalevich.model.local.Stoa;
import com.redmadintern.mikhalevich.utils.builders.EventStatusBuilder;
import com.redmadintern.mikhalevich.utils.builders.StoaBuilder;

import java.util.Calendar;

/**
 * Created by Alexander on 04.07.2015.
 */
public class MockUtil {

    public static EventReport createEventReportMock(long reportId) {
        //Mock
        EventReport eventReport = new EventReport(reportId, "№4576/3765/2642");

        //Status: application registered
        EventStatusBuilder eventStatusBuilder = new EventStatusBuilder()
                .setId(1234)
                .setTitle("Заявление зарегестрировано")
                .setShortDescription("№4576/3765/2642")
                .setSortNumber("0")
                .setPassed(true);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2015);
        calendar.set(Calendar.MONTH, 3);
        calendar.set(Calendar.DAY_OF_MONTH, 25);
        eventStatusBuilder.setDate(DateUtil.encodeDate(calendar));

        EventStatus eventStatus = eventStatusBuilder.createEventStatus();
        eventReport.addStatus(eventStatus);

        //Status: review appointed
        eventStatusBuilder = new EventStatusBuilder()
                .setId(2341)
                .setTitle("Назначен осмотр")
                .setShortDescription("ООО  «Автотрейд-АГ»")
                .setSortNumber("1")
                .setPassed(true);

        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2015);
        calendar.set(Calendar.MONTH, 3);
        calendar.set(Calendar.DAY_OF_MONTH, 26);
        eventStatusBuilder.setDate(DateUtil.encodeDate(calendar));

        Stoa stoa = new StoaBuilder()
                .setId(123)
                .setTitle("ООО  «Автотрейд-АГ»")
                .setAdress("Ул. Нагатинская, дом 16, корп. 1, стр. 5")
                .setServiceHours("Ежедневно, с 10:00 до 23:00")
                .setPhone("+7 495 995 12 20")
                .createStoa();

        eventStatusBuilder.setStoa(stoa);

        eventStatus = eventStatusBuilder.createEventStatus();
        eventReport.addStatus(eventStatus);

        //Status: provide docs
        eventStatusBuilder = new EventStatusBuilder()
                .setId(7654)
                .setTitle("Необходимо предоставить оригиналы документов")
                .setShortDescription("• Акт медицинского осведетельствования на состояние алкогольного опьянения" +
                        "\n• Справка об участии в дорожно-транспортном происшествии")
                .setSortNumber("2")
                .setPassed(false);

        eventStatus = eventStatusBuilder.createEventStatus();
        eventReport.addStatus(eventStatus);

        //Status: expect decision
        eventStatusBuilder = new EventStatusBuilder()
                .setId(345)
                .setTitle("Ожидайте решение");

        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2015);
        calendar.set(Calendar.MONTH, 4);
        calendar.set(Calendar.DAY_OF_MONTH, 25);
        eventStatusBuilder.setDate(DateUtil.encodeDate(calendar));

        eventStatus = eventStatusBuilder.createEventStatus();
        eventReport.addStatus(eventStatus);

        return eventReport;
    }

    public static EventStatus createStatus() {
        EventStatusBuilder eventStatusBuilder = new EventStatusBuilder()
                .setId(1234)
                .setTitle("Заявление зарегестрировано")
                .setShortDescription("№4576/3765/2642")
                .setSortNumber("0")
                .setPassed(true);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2015);
        calendar.set(Calendar.MONTH, 3);
        calendar.set(Calendar.DAY_OF_MONTH, 25);
        eventStatusBuilder.setDate(DateUtil.encodeDate(calendar));

        EventStatus eventStatus = eventStatusBuilder.createEventStatus();
        return eventStatus;
    }
}
