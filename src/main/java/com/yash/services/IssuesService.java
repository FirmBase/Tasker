package com.yash.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.yash.dtos.IssueDTO;

public interface IssuesService {
	public List<IssueDTO> allIssues(final Connection connection) throws SQLException;
	public boolean raiseIssue(final Connection connection, final Long taskId, final Long subtaskId, final String subject, final String description) throws SQLException;
	public boolean addressIssue(final Connection connection, final long id, final boolean resolveState) throws SQLException;
}
