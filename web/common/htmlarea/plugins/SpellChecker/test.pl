#! /usr/bin/perl -w


use Text::Aspell;
use Data::Dumper;

my $speller = new Text::Aspell;

my $dict = 'en';

# add configurable option for this
#$speller->set_option('lang', $dict);
#$speller->set_option('encoding', 'UTF-8');
#setlocale(LC_CTYPE, $dict);

# ultra, fast, normal, bad-spellers
# bad-spellers seems to cause segmentation fault
$speller->set_option('sug-mode', 'normal');

my @dicts = $speller->dictionary_info();
my $dictionaries = '';

$speller->print_config || $speller->errstr;

@dicts = $speller->dictionary_info;
print Data::Dumper::Dumper( \@dicts );

foreach my $i (@dicts) {
    next if $i->{jargon};
    my $name = $i->{name};
    if ($name eq $dict) {
        $name = '@'.$name;
    }
    $dictionaries .= ',' . $name;
}
$dictionaries =~ s/^,//;

print qq^
Test $dictionaries
^;


