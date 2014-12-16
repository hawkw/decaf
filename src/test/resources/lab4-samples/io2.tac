main:
	BeginFunc 8 ;
	_tmp0 = LCall _ReadLine ;
	_tmp1 = LCall _ReadLine ;
	PushParam _tmp1 ;
	LCall _PrintString ;
	PopParams 4 ;
	EndFunc ;
