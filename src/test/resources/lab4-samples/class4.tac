B.____GetZ:
	BeginFunc 4 ;
	_tmp0 = *(this + 4) ;
	Return _tmp0 ;
	EndFunc ;
VTable B =
	B.____GetZ,
; 
____binky:
	BeginFunc 20 ;
	_tmp1 = "x = " ;
	PushParam _tmp1 ;
	LCall _PrintString ;
	PopParams 4 ;
	PushParam x ;
	_tmp2 = *(x) ;
	_tmp3 = *(_tmp2) ;
	_tmp4 = ACall _tmp3 ;
	PopParams 4 ;
	PushParam _tmp4 ;
	LCall _PrintInt ;
	PopParams 4 ;
	_tmp5 = "\n" ;
	PushParam _tmp5 ;
	LCall _PrintString ;
	PopParams 4 ;
	EndFunc ;
X.____f:
	BeginFunc 4 ;
	_tmp6 = 3 ;
	*(this + 4) = _tmp6 ;
	PushParam this ;
	LCall ____binky ;
	PopParams 4 ;
	EndFunc ;
X.____compare:
	BeginFunc 4 ;
	_tmp7 = this == other ;
	Return _tmp7 ;
	EndFunc ;
VTable X =
	B.____GetZ,
	X.____f,
	X.____compare,
; 
main:
	BeginFunc 64 ;
	_tmp8 = 4 ;
	_tmp9 = 4 ;
	_tmp10 = _tmp9 + _tmp8 ;
	PushParam _tmp10 ;
	_tmp11 = LCall _Alloc ;
	PopParams 4 ;
	_tmp12 = X ;
	*(_tmp11) = _tmp12 ;
	d = _tmp11 ;
	PushParam d ;
	_tmp13 = *(d) ;
	_tmp14 = *(_tmp13 + 4) ;
	ACall _tmp14 ;
	PopParams 4 ;
	PushParam d ;
	PushParam d ;
	_tmp15 = *(d) ;
	_tmp16 = *(_tmp15 + 8) ;
	_tmp17 = ACall _tmp16 ;
	PopParams 8 ;
	IfZ _tmp17 Goto _L0 ;
	_tmp18 = "Same" ;
	PushParam _tmp18 ;
	LCall _PrintString ;
	PopParams 4 ;
	Goto _L1 ;
_L0:
	_tmp19 = "Different" ;
	PushParam _tmp19 ;
	LCall _PrintString ;
	PopParams 4 ;
_L1:
	EndFunc ;
