/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.appointment.api.db.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openmrs.module.appointment.Appointment;
import org.openmrs.module.appointment.AppointmentStatusHistory;
import org.openmrs.module.appointment.Appointment.AppointmentStatus;
import org.openmrs.module.appointment.api.db.AppointmentStatusHistoryDAO;
import org.springframework.transaction.annotation.Transactional;

public class HibernateAppointmentStatusHistoryDAO extends HibernateSingleClassDAO implements AppointmentStatusHistoryDAO {
	
	public HibernateAppointmentStatusHistoryDAO() {
		super(AppointmentStatusHistory.class);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<AppointmentStatusHistory> getAll(AppointmentStatus status) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(mappedClass);
		criteria.add(Restrictions.eq("status", status));
		criteria.addOrder(Order.asc("status"));
		return criteria.list();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Date getStartDateOfCurrentStatus(Appointment appointment) {
		String query = "Select Max(endDate) from AppointmentStatusHistory where appointment=:appointment";
		Date endDate = (Date) super.sessionFactory.getCurrentSession().createQuery(query).setParameter("appointment",
		    appointment).uniqueResult();
		endDate = (endDate == null && appointment != null) ? appointment.getDateCreated() : endDate;
		
		return endDate;
	}
	
}
