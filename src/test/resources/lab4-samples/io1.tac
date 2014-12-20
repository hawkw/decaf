main:
	BeginFunc 8 ;
	_tmp0 = LCall _ReadInteger ;
	_tmp1 = LCall _ReadInteger ;
	PushParam _tmp1 ;
	LCall _PrintInt ;
	PopParams 4 ;
	EndFunc ;
