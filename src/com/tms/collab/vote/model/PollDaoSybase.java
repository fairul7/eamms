package com.tms.collab.vote.model;

public class PollDaoSybase extends PollDao {
	public void updateAnswer(Answer answer) {
		try {
			String sql = "UPDATE poll_answer " +
					"SET answer = #answer#, q_id = #q_id#, total=#total#, piority=#piority# " +
					"WHERE id = " + answer.getId();
			super.update(sql,answer);
		} catch(Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public void deleteAnswer(Answer answer) {
		try {
			String sql = "DELETE FROM poll_answer WHERE id = " + answer.getId();
			super.update(sql,answer);
		} catch(Exception e) {
			log.error(e.getMessage(),e);
		}
	}
}
