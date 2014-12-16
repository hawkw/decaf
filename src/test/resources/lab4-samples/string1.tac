main:
	BeginFunc 16 ;
	_tmp0 = "hello" ;
	s = _tmp0 ;
	PushParam s ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp1 = LCall _ReadLine ;
	t = _tmp1 ;
	PushParam t ;
	LCall _PrintString ;
	PopParams 4 ;
	EndFunc ;
