package com.golding.tvbvotingsystem.interfaces;

public interface IVotingInfoDisplay {
	public void setVotingInfoDisplay(String votingname,String votinglogo);
	public void startVoting();
	public void submitVotingResult(String code);
	public void checkPswResult(String code);
}
