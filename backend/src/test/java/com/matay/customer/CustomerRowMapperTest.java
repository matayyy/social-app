package com.matay.customer;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        //given
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getInt(("id"))).thenReturn(1);
        when(resultSet.getInt(("age"))).thenReturn(24);
        when(resultSet.getString("name")).thenReturn("Gabe");
        when(resultSet.getString("email")).thenReturn("gabe@mail.com");
        when(resultSet.getString("gender")).thenReturn("MALE");

        //when
        Customer actual = customerRowMapper.mapRow(resultSet, 1);

        //then
        Customer expected = new Customer(1, "Gabe", "gabe@mail.com", 24, Gender.MALE);

        assertThat(actual).isEqualTo(expected);
    }
}