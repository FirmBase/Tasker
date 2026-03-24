package com.yash.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.yash.dtos.FeedbackDTO;

public interface FeedbackService {
	public boolean putFeedbackUser(Connection connection, long subtaskId, Short rating, String remarks) throws SQLException;
	public List<FeedbackDTO> getFeedbacksByUserId(Connection connection, Long userCredentialId) throws SQLException;
	public List<FeedbackDTO> getFeedbacksBySubtaskId(Connection connection, Long userCredentialId) throws SQLException;
	public boolean removeUserFeedback(final Connection connection, final long id) throws SQLException;
}
